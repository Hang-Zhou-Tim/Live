package org.hang.live.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.api.service.IShopInfoService;
import org.hang.live.api.vo.req.PrepareOrderVO;
import org.hang.live.api.vo.req.ShopCartReqVO;
import org.hang.live.api.vo.req.SkuInfoReqVO;
import org.hang.live.api.vo.resp.ShopCartRespVO;
import org.hang.live.api.vo.resp.SkuDetailInfoVO;
import org.hang.live.api.vo.resp.SkuInfoVO;
import org.hang.live.common.interfaces.utils.ConvertBeanUtils;
import org.hang.live.gift.dto.*;
import org.hang.live.gift.interfaces.IShopCartRPC;
import org.hang.live.gift.interfaces.ISkuInfoRPC;
import org.hang.live.gift.interfaces.ISkuOrderInfoRPC;
import org.hang.live.gift.interfaces.ISkuStockInfoRPC;
import org.hang.live.common.web.configuration.context.RequestContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
@Service
public class ShopInfoServiceImpl implements IShopInfoService {
    
    @DubboReference
    private ISkuInfoRPC skuInfoRPC;
    @DubboReference
    private IShopCartRPC shopCartRPC;
    @DubboReference
    private ISkuOrderInfoRPC SkuOrderInfoRPC;
    @DubboReference
    private ISkuStockInfoRPC skuStockInfoRPC;

    @Override
    public List<SkuInfoVO> queryByAnchorId(Long anchorId) {
        List<SkuInfoDTO> skuInfoDTOS = skuInfoRPC.queryByAnchorId(anchorId);
        return ConvertBeanUtils.convertList(skuInfoDTOS, SkuInfoVO.class);
    }

    @Override
    public SkuDetailInfoVO querySkuDetailById(SkuInfoReqVO skuInfoReqVO) {
        return ConvertBeanUtils.convert(skuInfoRPC.queryBySkuId(skuInfoReqVO.getSkuId(), skuInfoReqVO.getAnchorId()), SkuDetailInfoVO.class);
    }

    @Override
    public Boolean addItemToCart(ShopCartReqVO reqVO) {
        return shopCartRPC.addItemToCart(new ShopCartReqDTO(RequestContext.getUserId(), reqVO.getSkuId(), reqVO.getRoomId()));
    }

    @Override
    public Boolean removeItemFromCart(ShopCartReqVO reqVO) {
        return shopCartRPC.removeItemFromCart(new ShopCartReqDTO(RequestContext.getUserId(), reqVO.getSkuId(), reqVO.getRoomId()));
    }

    @Override
    public Boolean clearCart(ShopCartReqVO reqVO) {
        return shopCartRPC.clearCart(new ShopCartReqDTO(RequestContext.getUserId(), reqVO.getSkuId(), reqVO.getRoomId()));
    }

    @Override
    public Boolean increaseItemNumberInCart(ShopCartReqVO reqVO) {
        return shopCartRPC.increaseItemNumberInCart(new ShopCartReqDTO(RequestContext.getUserId(), reqVO.getSkuId(), reqVO.getRoomId()));
    }

    @Override
    public ShopCartRespVO getCartInfo(ShopCartReqVO reqVO) {
        ShopCartRespDTO carInfo = shopCartRPC.getCartInfo(new ShopCartReqDTO(RequestContext.getUserId(), reqVO.getSkuId(), reqVO.getRoomId()));
        ShopCartRespVO respVO = ConvertBeanUtils.convert(carInfo, ShopCartRespVO.class);
        respVO.setTotalPrice(carInfo.getTotalPrice());
        respVO.setShopCartItemRespDTOS(carInfo.getSkuCarItemRespDTOS());
        return respVO;
    }

    @Override
    public SkuPrepareOrderInfoDTO prepareOrder(PrepareOrderVO prepareOrderVO) {
        PrepareOrderReqDTO reqDTO = new PrepareOrderReqDTO();
        reqDTO.setRoomId(prepareOrderVO.getRoomId());
        reqDTO.setUserId(RequestContext.getUserId());
        return SkuOrderInfoRPC.prepareOrder(reqDTO);
    }

    @Override
    public boolean prepareStock(Long anchorId) {
        return skuStockInfoRPC.prepareStockInfo(anchorId);
    }

    @Override
    public boolean payNow(PrepareOrderVO prepareOrderVO) {
        return SkuOrderInfoRPC.payNow(RequestContext.getUserId(), prepareOrderVO.getRoomId());
    }
}