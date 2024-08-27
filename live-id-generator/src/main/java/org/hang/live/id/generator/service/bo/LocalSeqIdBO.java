package org.hang.live.id.generator.service.bo;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/11
 * @Description Sequential id Business Object
 */
public class LocalSeqIdBO {

    private int id;
    /**
     * Get current in-memory-id
     */
    private AtomicLong currentNum;

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

    public AtomicLong getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(AtomicLong currentNum) {
        this.currentNum = currentNum;
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
