package org.hang.live.api.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.api.service.IBankService;
import org.hang.live.api.vo.req.PayProductReqVO;
import org.hang.live.api.vo.resp.PayProductItemVO;
import org.hang.live.api.vo.resp.PayProductRespVO;
import org.hang.live.api.vo.resp.PayProductVO;
import org.hang.live.user.payment.constants.OrderStatusEnum;
import org.hang.live.user.payment.constants.PaySourceEnum;
import org.hang.live.user.payment.dto.PayOrderDTO;
import org.hang.live.user.payment.dto.PayProductDTO;
import org.hang.live.user.payment.interfaces.IPayOrderRpc;
import org.hang.live.user.payment.interfaces.IPayProductRpc;
import org.hang.live.user.payment.interfaces.IAccountBalanceRpc;
import org.hang.live.common.web.configuration.context.RequestContext;
import org.hang.live.common.web.configuration.error.BizBaseErrorEnum;
import org.hang.live.common.web.configuration.error.ErrorAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/25
 * @Description
 */
@Service
public class BankServiceImpl implements IBankService {

    @DubboReference(timeout = 3000)
    private IPayProductRpc payProductRpc;
    @DubboReference
    private IAccountBalanceRpc accountBalanceRpc;
    @DubboReference
    private IPayOrderRpc payOrderRpc;
    @Resource
    private RestTemplate restTemplate;

    @Value("${hang.payment.callback.url}")
    private String url;

    @Value("${hang.payment.callback.port}")
    private String port;

    @Override
    public PayProductVO products(Integer type) {
        //Get list of products for users to pay with info including name, number.
        List<PayProductDTO> payProductDTOS = payProductRpc.products(type);
        PayProductVO payProductVO = new PayProductVO();
        List<PayProductItemVO> itemList = new ArrayList<>();
        for (PayProductDTO payProductDTO : payProductDTOS) {
            PayProductItemVO itemVO = new PayProductItemVO();
            itemVO.setName(payProductDTO.getName());
            itemVO.setId(payProductDTO.getId());
            itemVO.setCoinNum(JSON.parseObject(payProductDTO.getExtra()).getInteger("coin"));
            itemList.add(itemVO);
        }
        payProductVO.setPayProductItemVOList(itemList);
        //Also Refresh User's Balance
        payProductVO.setCurrentBalance(Optional.ofNullable(accountBalanceRpc.getBalance(RequestContext.getUserId())).orElse(0));
        return payProductVO;
    }

    @Override
    public PayProductRespVO payProduct(PayProductReqVO payProductReqVO) {
        //Validate the product that user wants to pay
        ErrorAssert.isTure(payProductReqVO != null && payProductReqVO.getProductId() != null && payProductReqVO.getPaySource() != null, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isNotNull(PaySourceEnum.find(payProductReqVO.getPaySource()), BizBaseErrorEnum.PARAM_ERROR);
        //Get the product info
        PayProductDTO payProductDTO = payProductRpc.getByProductId(payProductReqVO.getProductId());
        ErrorAssert.isNotNull(payProductDTO, BizBaseErrorEnum.PARAM_ERROR);

        //Insert an product payment order
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setProductId(payProductReqVO.getProductId());
        payOrderDTO.setUserId(RequestContext.getUserId());
        payOrderDTO.setSource(payProductReqVO.getPaySource());
        payOrderDTO.setPayChannel(payProductReqVO.getPayChannel());
        String orderId = payOrderRpc.insertOne(payOrderDTO);

        //Change order status to "Paying"
        payOrderRpc.updateOrderStatus(orderId, OrderStatusEnum.PAYING.getCode());
        PayProductRespVO payProductRespVO = new PayProductRespVO();
        payProductRespVO.setOrderId(orderId);

        //I used rest-template to simulate third-party payment finished callback.
        //The url pointed to payment-callback controller's url, where receives payment results and apply callback event.
        //For example if there is user-interest business requirement, then the callback module could call down-streaming microservice to analyse this payment.
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", orderId);
        jsonObject.put("userId", RequestContext.getUserId());
        jsonObject.put("bizCode", 10001);
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("param",jsonObject.toJSONString());
        ResponseEntity<String> resultEntity = restTemplate.postForEntity("http://" + url + ":" + port + "/payment/callback/paymentNotify/callback?param={param}", null, String.class,paramMap);
        System.out.println(resultEntity.getBody());
        return payProductRespVO;
    }
}
