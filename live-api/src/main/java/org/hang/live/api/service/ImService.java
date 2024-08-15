package org.hang.live.api.service;

import org.hang.live.api.vo.resp.ImConfigVO;

/**
 * @Author hang
 * @Date: Created in 10:48 2024/8/28
 * @Description
 */
public interface ImService {
    //Return the ws server address and im token
    ImConfigVO getImConfig();
}
