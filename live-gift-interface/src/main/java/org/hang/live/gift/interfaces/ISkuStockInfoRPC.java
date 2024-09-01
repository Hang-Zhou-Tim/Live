package org.hang.live.gift.interfaces;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/30
 * @Description
 */
public interface ISkuStockInfoRPC {

    /**
     * Decrease stock number by sku Id
     */
    boolean decreaseStockNumberBySkuId(Long skuId, Integer num);

    /**
     * Prepare live stream marketing: put stock info that anchor sells into Redis
     */
    boolean prepareStockInfo(Long anchorId);

    /**
     * Query remaining stock number by sku id.
     */
    Integer queryStockNumber(Long skuId);

    /**
     * Synchronise the Stock Number from redis to MySQL
     */
    boolean syncStockNumberToMySQL(Long anchor);
}