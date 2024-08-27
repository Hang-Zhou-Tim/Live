package org.hang.live.id.generator.service;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/11
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
