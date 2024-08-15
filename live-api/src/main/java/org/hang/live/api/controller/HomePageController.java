package org.hang.live.api.controller;

import jakarta.annotation.Resource;
import org.hang.live.api.service.IHomePageService;
import org.hang.live.api.vo.HomePageVO;
import org.hang.live.common.interfaces.vo.WebResponseVO;
import org.hang.live.web.starter.context.RequestContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author hang
 * @Date: Created in 08:56 2024/8/12
 * @Description
 */
@RestController
@RequestMapping("/live/api/home")
public class HomePageController {

    @Resource
    private IHomePageService homePageService;

    @PostMapping("/initPage")
    public WebResponseVO initPage() {
        Long userId = RequestContext.getUserId();
        HomePageVO homePageVO = new HomePageVO();
        homePageVO.setLoginStatus(false);
        if (userId != null) {
            homePageVO = homePageService.initPage(userId);
            homePageVO.setLoginStatus(true);
        }
        return WebResponseVO.success(homePageVO);
    }
}
