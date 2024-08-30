package org.hang.live.api.vo.req;


import org.hang.live.user.payment.constants.PayChannelEnum;
import org.hang.live.user.payment.constants.PaySourceEnum;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public class BuyCurrencyReqVO {

    private Integer currencyId;

    /**
     * payment source (pay initialise in user center or live stream room?)
     * @see PaySourceEnum
     */
    private Integer paySource;

    /**
     * payment channel (pay finished by which third party channel?)
     * @see PayChannelEnum
     */
    private Integer payChannel;

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getPaySource() {
        return paySource;
    }

    public void setPaySource(Integer paySource) {
        this.paySource = paySource;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    @Override
    public String toString() {
        return "PayProductReqVO{" +
                "currencyId=" + currencyId +
                ", payChannel=" + payChannel +
                ", paySource=" + paySource +
                '}';
    }
}
