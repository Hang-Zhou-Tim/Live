package org.hang.live.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.hang.live.api.service.IHomePageService;
import org.hang.live.api.vo.HomePageVO;
import org.hang.live.user.constants.UserTagsEnum;
import org.hang.live.user.dto.UserDTO;
import org.hang.live.user.interfaces.IUserRPC;
import org.hang.live.user.interfaces.IUserTagRPC;
import org.springframework.stereotype.Service;

/**
 * @Author hang
 * @Date: Created in 23:03 2024/8/12
 * @Description
 */
@Service
public class HomePageServiceImpl implements IHomePageService {

    @DubboReference
    private IUserRPC userRpc;
    @DubboReference
    private IUserTagRPC userTagRpc;

    @Override
    public HomePageVO initPage(Long userId) {
        UserDTO userDTO = userRpc.getByUserId(userId);
        HomePageVO homePageVO = new HomePageVO();
        if (userDTO != null) {
            homePageVO.setAvatar(userDTO.getAvatar());
            homePageVO.setUserId(userId);
            homePageVO.setNickName(userDTO.getNickName());
            //Only anchor who is VIP can broadcast.
            homePageVO.setShowStartLivingBtn(userTagRpc.containTag(userId, UserTagsEnum.IS_VIP));
        }
        return homePageVO;
    }
}
