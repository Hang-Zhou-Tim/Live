package org.hang.live.api.vo.resp;

import java.util.List;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public class PayProductVO {

    /**
     * user's remaining balance
     */
    private Integer currentBalance;

    /**
     * a list of products user want to buy
     */
    private List<PayProductItemVO> payProductItemVOList;

    public Integer getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Integer currentBalance) {
        this.currentBalance = currentBalance;
    }

    public List<PayProductItemVO> getPayProductItemVOList() {
        return payProductItemVOList;
    }

    public void setPayProductItemVOList(List<PayProductItemVO> payProductItemVOList) {
        this.payProductItemVOList = payProductItemVOList;
    }
}
