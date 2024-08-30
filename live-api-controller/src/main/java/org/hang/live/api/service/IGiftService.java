package org.hang.live.api.service;

import org.hang.live.api.vo.req.GiftReqVO;
import org.hang.live.api.vo.resp.GiftConfigVO;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public interface IGiftService {
    //Get all gifts that the room contains.
    List<GiftConfigVO> listGift();
    //Send gifts in the room.
    boolean send(GiftReqVO giftReqVO);
}