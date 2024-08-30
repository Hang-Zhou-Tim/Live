package org.hang.live.api.vo.req;


import org.hang.live.user.payment.constants.PayChannelEnum;
import org.hang.live.user.payment.constants.PaySourceEnum;

/**
 * @Author hang
 * @Date: Created in 22:54 2024/8/21
 * @Description
 */
public class PayProductReqVO {

    private Integer productId;

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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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
                "productId=" + productId +
                ", payChannel=" + payChannel +
                ", paySource=" + paySource +
                '}';
    }
}
