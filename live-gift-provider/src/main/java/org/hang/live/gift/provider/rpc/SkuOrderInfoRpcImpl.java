package org.hang.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hang.live.user.payment.interfaces.IAccountBalanceRPC;
import org.hang.live.common.interfaces.topic.GiftProviderTopicNames;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.gift.provider.service.bo.RollBackStockBO;
import org.hang.live.gift.constants.SkuOrderInfoEnum;
import org.hang.live.gift.dto.*;
import org.hang.live.gift.interfaces.ISkuOrderInfoRPC;
import org.hang.live.gift.provider.dao.po.SkuInfoPO;
import org.hang.live.gift.provider.dao.po.SkuOrderInfoPO;
import org.hang.live.gift.provider.service.IShopCartService;
import org.hang.live.gift.provider.service.ISkuInfoService;
import org.hang.live.gift.provider.service.ISkuOrderInfoService;
import org.hang.live.gift.provider.service.ISkuStockInfoService;
import org.hang.live.gift.provider.service.impl.RedPacketConfigServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@DubboService
public class SkuOrderInfoRpcImpl implements ISkuOrderInfoRPC {
    @Resource
    private MQProducer mqProducer;
    @Resource
    private ISkuOrderInfoService skuOrderInfoService;
    @Resource
    private IShopCartService shopCartService;
    @Resource
    private ISkuStockInfoService skuStockInfoService;
    @Resource
    private ISkuInfoService skuInfoService;
    @Resource
    private IAccountBalanceRPC accountBalanceRPC;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedPacketConfigServiceImpl.class);



    @Override
    public SkuOrderInfoRespDTO queryByUserIdAndRoomId(Long userId, Integer roomId) {
        return skuOrderInfoService.queryByUserIdAndRoomId(userId, roomId);
    }

    @Override
    public boolean insertOne(SkuOrderInfoReqDTO skuOrderInfoReqDTO) {
        return skuOrderInfoService.insertOne(skuOrderInfoReqDTO) != null;
    }

    @Override
    public boolean updateOrderStatus(SkuOrderInfoReqDTO skuOrderInfoReqDTO) {
        return updateOrderStatus(skuOrderInfoReqDTO);
    }

    @Override
    public SkuPrepareOrderInfoDTO prepareOrder(PrepareOrderReqDTO reqDTO) {
        ShopCartReqDTO shopCarReqDTO = ConvertBeanUtils.convert(reqDTO, ShopCartReqDTO.class);
        ShopCartRespDTO cartInfo = shopCartService.getCartInfo(shopCarReqDTO);
        List<ShopCartItemRespDTO> cartItemList = cartInfo.getSkuCarItemRespDTOS();
        if (CollectionUtils.isEmpty(cartItemList)) {
            return new SkuPrepareOrderInfoDTO();
        }
        List<Long> skuIdList = cartItemList.stream().map(item -> item.getSkuInfoDTO().getSkuId()).collect(Collectors.toList());
        Iterator<Long> iterator = skuIdList.iterator();
        // Decrease stock number for all
        while (iterator.hasNext()) {
            Long skuId = iterator.next();
            boolean isSuccess = skuStockInfoService.decreaseStockNumberBySkuIdByLua(skuId, 1);
            if (!isSuccess) iterator.remove();
        }
        // Insert the new order recording this payment.
        SkuOrderInfoPO skuOrderInfoPO = skuOrderInfoService.insertOne(new SkuOrderInfoReqDTO(
                null, reqDTO.getUserId(), reqDTO.getRoomId(), SkuOrderInfoEnum.PREPARE_PAY.getCode(), skuIdList));
        // Clear cart
        shopCartService.clearCart(shopCarReqDTO);

        // Send delayed message to MQ, so the consumer can roll back the stock if the user does not pay the order
        RollBackStockBO rollBackStockBO = new RollBackStockBO(reqDTO.getUserId(), skuOrderInfoPO.getId());
        Message message = new Message();
        message.setTopic(GiftProviderTopicNames.ROLL_BACK_STOCK);
        message.setBody(com.alibaba.fastjson2.JSON.toJSONBytes(rollBackStockBO));
        message.setDelayTimeLevel(14);
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(message);
            LOGGER.info("[insertOne] sendResult is {}", sendResult);
        } catch (Exception e) {}

        // calculate general information for the cart and return them.
        SkuPrepareOrderInfoDTO respDTO = new SkuPrepareOrderInfoDTO();
        List<ShopCartItemRespDTO> itemList = cartItemList.stream().filter(item -> skuIdList.contains(item.getSkuInfoDTO().getSkuId())).collect(Collectors.toList());
        respDTO.setSkuPrepareOrderItemInfoDTOS(itemList);
        respDTO.setTotalPrice(itemList.stream().map(item -> item.getSkuInfoDTO().getSkuPrice()).reduce(Integer::sum).orElse(0));
        return respDTO;
    }

    @Override
    public boolean payNow(Long userId, Integer roomId) {
        SkuOrderInfoRespDTO skuOrderInfo = skuOrderInfoService.queryByUserIdAndRoomId(userId, roomId);
        // check if the status is "prepare"
        if (!skuOrderInfo.getStatus().equals(SkuOrderInfoEnum.PREPARE_PAY.getCode())) {
            return false;
        }
        // get sku id list
        List<Long> skuIdList = Arrays.stream(skuOrderInfo.getSkuIdList().split(",")).map(Long::valueOf).collect(Collectors.toList());
        // get sku info po
        List<SkuInfoPO> skuInfoPOS = skuInfoService.queryBySkuIds(skuIdList);
        // calculate the total price of sku for these products
        Integer totalPrice = skuInfoPOS.stream().map(SkuInfoPO::getSkuPrice).reduce(Integer::sum).orElse(0);
        // check if the user has enough balance
        Integer balance = accountBalanceRPC.getBalance(userId);
        if (balance < totalPrice) {
            return false;
        }
        // deduct balance
        accountBalanceRPC.decrementBalance(userId, totalPrice);
        // set the status of order to finish, so it cannot be rollback.
        SkuOrderInfoReqDTO reqDTO = ConvertBeanUtils.convert(skuOrderInfo, SkuOrderInfoReqDTO.class);
        reqDTO.setStatus(SkuOrderInfoEnum.HAS_PAY.getCode());
        skuOrderInfoService.updateOrderStatus(reqDTO);
        return true;
    }
}