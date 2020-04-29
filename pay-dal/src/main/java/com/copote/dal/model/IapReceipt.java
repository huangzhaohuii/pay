package com.copote.dal.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class IapReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付订单号
     */
    private String payOrderId;

    /**
     * 商户ID
     */
    private String mchId;

    /**
     * IAP业务号
     */
    private String transactionId;

    /**
     * 处理状态:0-未处理,1-处理成功,-1-处理失败
     */
    private Byte status;

    /**
     * 处理次数
     */
    private Byte handleCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 渠道ID
     */
    private String receiptData;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", payOrderId=").append(payOrderId);
        sb.append(", mchId=").append(mchId);
        sb.append(", transactionId=").append(transactionId);
        sb.append(", status=").append(status);
        sb.append(", handleCount=").append(handleCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", receiptData=").append(receiptData);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        IapReceipt other = (IapReceipt) that;
        return (this.getPayOrderId() == null ? other.getPayOrderId() == null : this.getPayOrderId().equals(other.getPayOrderId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getTransactionId() == null ? other.getTransactionId() == null : this.getTransactionId().equals(other.getTransactionId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getHandleCount() == null ? other.getHandleCount() == null : this.getHandleCount().equals(other.getHandleCount()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getReceiptData() == null ? other.getReceiptData() == null : this.getReceiptData().equals(other.getReceiptData()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPayOrderId() == null) ? 0 : getPayOrderId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getTransactionId() == null) ? 0 : getTransactionId().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getHandleCount() == null) ? 0 : getHandleCount().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getReceiptData() == null) ? 0 : getReceiptData().hashCode());
        return result;
    }
}