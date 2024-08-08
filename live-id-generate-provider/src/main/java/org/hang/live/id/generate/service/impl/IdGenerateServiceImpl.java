package org.hang.live.id.generate.service.impl;

import jakarta.annotation.Resource;
import org.hang.live.id.generate.dao.mapper.IdGenerateMapper;
import org.hang.live.id.generate.dao.po.IdGeneratePO;
import org.hang.live.id.generate.service.IdGenerateService;
import org.hang.live.id.generate.service.bo.LocalSeqIdBO;
import org.hang.live.id.generate.service.bo.LocalUnSeqIdBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author hang
 * @Date: Created in 19:58 2023/5/25
 * @Description
 * When this service is initialised by Spring boot, it will request IDs related to this bussiness.
 * The RPC calls to get sequential/unsequential will get these ids.
 * When the id in memory is higher than 75%, the call will send asynchronous request to update the id segment.
 *
 */
@Service
public class IdGenerateServiceImpl implements IdGenerateService, InitializingBean {

    @Resource
    private IdGenerateMapper idGenerateMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(IdGenerateServiceImpl.class);
    private static Map<Integer, LocalSeqIdBO> localSeqIdBOMap = new ConcurrentHashMap<>();
    private static Map<Integer, LocalUnSeqIdBO> localUnSeqIdBOMap = new ConcurrentHashMap<>();
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("id-generate-thread-" + ThreadLocalRandom.current().nextInt(1000));
                    return thread;
                }
            });
    private static final float UPDATE_RATE = 0.75f;
    private static final int SEQ_ID = 1;
    private static Map<Integer, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    @Override
    public Long getUnSeqId(Integer id) {
        if (id == null) {
            LOGGER.error("[getSeqId] id is error,id is {}", id);
            return null;
        }
        LocalUnSeqIdBO localUnSeqIdBO = localUnSeqIdBOMap.get(id);
        if (localUnSeqIdBO == null) {
            LOGGER.error("[getUnSeqId] localUnSeqIdBO is null,id is {}", id);
            return null;
        }
        Long returnId = localUnSeqIdBO.getIdQueue().poll();
        if (returnId == null) {
            LOGGER.error("[getUnSeqId] returnId is null,id is {}", id);
            return null;
        }
        this.refreshLocalUnSeqId(localUnSeqIdBO);
        return returnId;
    }

    @Override
    public Long getSeqId(Integer id) {
        if (id == null) {
            LOGGER.error("[getSeqId] id is error,id is {}", id);
            return null;
        }
        LocalSeqIdBO localSeqIdBO = localSeqIdBOMap.get(id);
        if (localSeqIdBO == null) {
            LOGGER.error("[getSeqId] localSeqIdBO is null,id is {}", id);
            return null;
        }
        this.refreshLocalSeqId(localSeqIdBO);
        long returnId = localSeqIdBO.getCurrentNum().incrementAndGet();
        if (returnId > localSeqIdBO.getNextThreshold()) {
            //synchronised for update
            LOGGER.error("[getSeqId] returned id is over limit, returned id is {}", returnId);
            return null;
        }
        return returnId;
    }

    /**
     * Refresh the current id segment
     *
     * @param localSeqIdBO
     */
    private void refreshLocalSeqId(LocalSeqIdBO localSeqIdBO) {
        long step = localSeqIdBO.getNextThreshold() - localSeqIdBO.getCurrentStart();
        if (localSeqIdBO.getCurrentNum().get() - localSeqIdBO.getCurrentStart() > step * UPDATE_RATE) {
            Semaphore semaphore = semaphoreMap.get(localSeqIdBO.getId());
            if (semaphore == null) {
                LOGGER.error("semaphore is null,id is {}", localSeqIdBO.getId());
                return;
            }
            boolean acquireStatus = semaphore.tryAcquire();
            if (acquireStatus) {
                LOGGER.info("Start update id segment asynchronously");
                //asynchronously update id segment;
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdGeneratePO idGeneratePO = idGenerateMapper.selectById(localSeqIdBO.getId());
                            tryUpdateMySQLRecord(idGeneratePO);
                        } catch (Exception e) {
                            LOGGER.error("[refreshLocalSeqId] error is ", e);
                        } finally {
                            semaphoreMap.get(localSeqIdBO.getId()).release();
                            LOGGER.info("Current sequential id segment is updated; id segment is {}", localSeqIdBO.getId());
                        }
                    }
                });
            }
        }
    }

    /**
     * Refresh current id segment.
     *
     * @param localUnSeqIdBO
     */
    private void refreshLocalUnSeqId(LocalUnSeqIdBO localUnSeqIdBO) {
        long begin = localUnSeqIdBO.getCurrentStart();
        long end = localUnSeqIdBO.getNextThreshold();
        long remainSize = localUnSeqIdBO.getIdQueue().size();
        //If the remaining space is below 25%，it will get new id segment and refresh it with another threads.
        if ((end - begin) * 0.25 > remainSize) {
            Semaphore semaphore = semaphoreMap.get(localUnSeqIdBO.getId());
            if (semaphore == null) {
                LOGGER.error("semaphore is null,id is {}", localUnSeqIdBO.getId());
                return;
            }
            boolean acquireStatus = semaphore.tryAcquire();
            if (acquireStatus) {
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdGeneratePO idGeneratePO = idGenerateMapper.selectById(localUnSeqIdBO.getId());
                            tryUpdateMySQLRecord(idGeneratePO);
                        } catch (Exception e) {
                            LOGGER.error("[refreshLocalUnSeqId] error is ", e);
                        } finally {
                            semaphoreMap.get(localUnSeqIdBO.getId()).release();
                            LOGGER.info("Local id segment is refreshed，id is {}", localUnSeqIdBO.getId());
                        }
                    }
                });
            }
        }
    }

    //After the bean is initialised, this methods will be invoked to get all id segment that the business reqires to.
    @Override
    public void afterPropertiesSet() throws Exception {
        List<IdGeneratePO> idGeneratePOList = idGenerateMapper.selectAll();
        for (IdGeneratePO idGeneratePO : idGeneratePOList) {
            LOGGER.info("Service starts，compete for id segment.");
            tryUpdateMySQLRecord(idGeneratePO);
            semaphoreMap.put(idGeneratePO.getId(), new Semaphore(1));
        }
    }

    /**
     * Update the id segment in database. Note this operation is implemented with the thinking of optimistic locks.
     * If the version is not the same, meaning other processes in other device has requests that id segment, it will make another requests.
     * @param idGeneratePO
     */
    private void tryUpdateMySQLRecord(IdGeneratePO idGeneratePO) {
        int updateResult = idGenerateMapper.updateNewIdCountAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
        if (updateResult > 0) {
            localIdBOHandler(idGeneratePO);
            return;
        }
        //Retry update for three times. It is very rate that the process still cannot get id segment. Maybe think of increment on segment length.
        for (int i = 0; i < 3; i++) {
            idGeneratePO = idGenerateMapper.selectById(idGeneratePO.getId());
            updateResult = idGenerateMapper.updateNewIdCountAndVersion(idGeneratePO.getId(), idGeneratePO.getVersion());
            if (updateResult > 0) {
                localIdBOHandler(idGeneratePO);
                return;
            }
        }
        throw new RuntimeException("Id segment update is failed. Current id segment is " + idGeneratePO.getId());
    }

    /**
     * Stores local BOs that encapsulates current id segment info.
     *
     * @param idGeneratePO
     */
    private void localIdBOHandler(IdGeneratePO idGeneratePO) {
        long currentStart = idGeneratePO.getCurrentStart();
        long nextThreshold = idGeneratePO.getNextThreshold();
        long currentNum = currentStart;
        if (idGeneratePO.getIsSeq() == SEQ_ID) {
            LocalSeqIdBO localSeqIdBO = new LocalSeqIdBO();
            AtomicLong atomicLong = new AtomicLong(currentNum);
            localSeqIdBO.setId(idGeneratePO.getId());
            localSeqIdBO.setNextThreshold(nextThreshold);
            localSeqIdBO.setCurrentNum(atomicLong);
            localSeqIdBO.setCurrentStart(currentStart);

            //Replace the id segment with new sequential id segment.
            localSeqIdBOMap.put(localSeqIdBO.getId(), localSeqIdBO);
        } else {
            LocalUnSeqIdBO localUnSeqIdBO = new LocalUnSeqIdBO();
            localUnSeqIdBO.setCurrentStart(currentStart);
            localUnSeqIdBO.setNextThreshold(nextThreshold);
            localUnSeqIdBO.setId(idGeneratePO.getId());
            long begin = localUnSeqIdBO.getCurrentStart();
            long end = localUnSeqIdBO.getNextThreshold();
            List<Long> idList = new ArrayList<>();
            for (long i = begin; i < end; i++) {
                idList.add(i);
            }
            //Shuffle the list, so that its order seems to be random.
            Collections.shuffle(idList);
            ConcurrentLinkedQueue<Long> idQueue = new ConcurrentLinkedQueue<>();
            idQueue.addAll(idList);
            localUnSeqIdBO.setIdQueue(idQueue);
            //Replace the id segment with new unseq id segment.
            localUnSeqIdBOMap.put(localUnSeqIdBO.getId(), localUnSeqIdBO);
        }
    }
}
