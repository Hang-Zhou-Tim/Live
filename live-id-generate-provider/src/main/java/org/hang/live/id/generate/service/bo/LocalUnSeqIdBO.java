package org.hang.live.id.generate.service.bo;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author idea
 * @Date: Created in 20:32 2023/5/26
 * @Description unodered id Bussiness Object
 */
public class LocalUnSeqIdBO {

    private int id;
    /**
     * Store disordered id in this queue
     */
    private ConcurrentLinkedQueue<Long> idQueue;
    /**
     * Starting value for current id segment
     */
    private Long currentStart;
    /**
     * Ending value for current id segment
     */
    private Long nextThreshold;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ConcurrentLinkedQueue<Long> getIdQueue() {
        return idQueue;
    }

    public void setIdQueue(ConcurrentLinkedQueue<Long> idQueue) {
        this.idQueue = idQueue;
    }

    public Long getCurrentStart() {
        return currentStart;
    }

    public void setCurrentStart(Long currentStart) {
        this.currentStart = currentStart;
    }

    public Long getNextThreshold() {
        return nextThreshold;
    }

    public void setNextThreshold(Long nextThreshold) {
        this.nextThreshold = nextThreshold;
    }
}
