package org.hang.live.api.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.api.service.IPaymentService;
import org.hang.live.api.vo.req.BuyCurrencyReqVO;
import org.hang.live.api.vo.resp.BuyCurrencyRespVO;
import org.hang.live.api.vo.resp.CurrencyAmountsVO;
import org.hang.live.api.vo.resp.CurrencyVO;
import org.hang.live.user.payment.constants.OrderStatusEnum;
import org.hang.live.user.payment.constants.PaySourceEnum;
import org.hang.live.user.payment.dto.CurrencyDTO;
import org.hang.live.user.payment.dto.PaymentOrderDTO;
import org.hang.live.user.payment.interfaces.*;
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
public class PaymentServiceImpl implements IPaymentService {

    @DubboReference(timeout = 3000)
    private ICurrencyRPC currencyRPC;
    @DubboReference
    private IAccountBalanceRPC accountBalanceRPC;
    @DubboReference
    private IPaymentOrderRPC paymentOrderRPC;

    @DubboReference
    private IPaymentCallbackServiceRPC PaymentCallbackServiceRPC;

    @Override
    public CurrencyVO getAllCurrencyAmounts(Integer type) {
        //Get list of products for users to pay with info including name, number.
        List<CurrencyDTO> currencyDTOS = currencyRPC.getAllCurrencyAmounts(type);
        CurrencyVO currencyVO = new CurrencyVO();
        List<CurrencyAmountsVO> itemList = new ArrayList<>();
        for (CurrencyDTO currencyDTO : currencyDTOS) {
            CurrencyAmountsVO itemVO = new CurrencyAmountsVO();
            itemVO.setName(currencyDTO.getName());
            itemVO.setId(currencyDTO.getId());
            itemVO.setCoinNum(JSON.parseObject(currencyDTO.getExtra()).getInteger("coin"));
            itemList.add(itemVO);
        }
        currencyVO.setCurrencyItemList(itemList);
        //Also Refresh User's Balance
        currencyVO.setCurrentBalance(Optional.ofNullable(accountBalanceRPC.getBalance(RequestContext.getUserId())).orElse(0));
        return currencyVO;
    }

    @Override
    public BuyCurrencyRespVO buyCurrency(BuyCurrencyReqVO buyCurrencyReqVO) {
        //Validate the product that user wants to pay
        ErrorAssert.isTure(buyCurrencyReqVO != null && buyCurrencyReqVO.getCurrencyId() != null && buyCurrencyReqVO.getPaySource() != null, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isNotNull(PaySourceEnum.find(buyCurrencyReqVO.getPaySource()), BizBaseErrorEnum.PARAM_ERROR);
        //Get the product info
        CurrencyDTO currencyDTO = currencyRPC.getByCurrencyId(buyCurrencyReqVO.getCurrencyId());
        ErrorAssert.isNotNull(currencyDTO, BizBaseErrorEnum.PARAM_ERROR);

        //Insert an product payment order
        PaymentOrderDTO paymentOrderDTO = new PaymentOrderDTO();
        paymentOrderDTO.setCurrencyId(buyCurrencyReqVO.getCurrencyId());
        paymentOrderDTO.setUserId(RequestContext.getUserId());
        paymentOrderDTO.setSource(buyCurrencyReqVO.getPaySource());
        paymentOrderDTO.setPayChannel(buyCurrencyReqVO.getPayChannel());
        String orderId = paymentOrderRPC.insertOne(paymentOrderDTO);

        //Change order status to "Paying"
        paymentOrderRPC.updateOrderStatus(orderId, OrderStatusEnum.PAYING.getCode());
        BuyCurrencyRespVO buyCurrencyRespVO = new BuyCurrencyRespVO();
        buyCurrencyRespVO.setOrderId(orderId);

        //I used rest-template to simulate third-party payment finished callback.
        //The url pointed to payment-callback controller's url, where receives payment results and apply callback event.
        //For example if there is user-interest business requirement, then the callback module could call down-streaming microservice to analyse this payment.
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", orderId);
        jsonObject.put("userId", RequestContext.getUserId());
        jsonObject.put("bizCode", 10001);
        String result = PaymentCallbackServiceRPC.paymentCallback(JSON.toJSONString(jsonObject));
        System.out.println(result);
        return buyCurrencyRespVO;
    }
}
