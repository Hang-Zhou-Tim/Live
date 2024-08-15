package org.hang.live.api.service;

import org.hang.live.api.vo.HomePageVO;

/**
 * @Author hang
 * @Date: Created in 23:02 2024/8/12
 * @Description
 */
public interface IHomePageService {


    /**
     * Get all initial information when user logs in.
     *
     * @param userId
     * @return
     */
    HomePageVO initPage(Long userId);


}
