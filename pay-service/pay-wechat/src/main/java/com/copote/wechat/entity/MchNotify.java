package com.copote.wechat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MchNotify implements Serializable {
    /**
     * 订单ID
     *
     * @mbggenerated
     */
    private String orderId;

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    private String mchId;

    /**
     * 商户订单号
     *
     * @mbggenerated
     */
    private String mchOrderNo;

    /**
     * 订单类型:1-支付,2-转账,3-退款
     *
     * @mbggenerated
     */
    private String orderType;

    /**
     * 通知地址
     *
     * @mbggenerated
     */
    private String notifyUrl;

    /**
     * 通知次数
     *
     * @mbggenerated
     */
    private Byte notifyCount;

    /**
     * 通知响应结果
     *
     * @mbggenerated
     */
    private String result;

    /**
     * 通知状态,1-通知中,2-通知成功,3-通知失败
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 最后一次通知时间
     *
     * @mbggenerated
     */
    private Date lastNotifyTime;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderId=").append(orderId);
        sb.append(", mchId=").append(mchId);
        sb.append(", mchOrderNo=").append(mchOrderNo);
        sb.append(", orderType=").append(orderType);
        sb.append(", notifyUrl=").append(notifyUrl);
        sb.append(", notifyCount=").append(notifyCount);
        sb.append(", result=").append(result);
        sb.append(", status=").append(status);
        sb.append(", lastNotifyTime=").append(lastNotifyTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

}