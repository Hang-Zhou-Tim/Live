package org.hang.live.id.generate.service;

/**
 * @Author hang
 * @Date: Created in 19:58 2023/5/25
 * @Description
 */
public interface IdGenerateService {

    /**
     * Get ordered id
     *
     * @param id
     * @return
     */
    Long getSeqId(Integer id);

    /**
     * Get unordered id
     *
     * @param id
     * @return
     */
    Long getUnSeqId(Integer id);
}
