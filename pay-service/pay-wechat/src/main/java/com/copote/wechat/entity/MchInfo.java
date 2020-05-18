package com.copote.wechat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MchInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 商户ID
     */
    private String mchId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 请求私钥
     */
    private String reqKey;

    /**
     * 响应私钥
     */
    private String resKey;

    /**
     * 商户状态,0-停止使用,1-使用中
     */
    private Byte state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mchId=").append(mchId);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", reqKey=").append(reqKey);
        sb.append(", resKey=").append(resKey);
        sb.append(", state=").append(state);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
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
        MchInfo other = (MchInfo) that;
        return (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getReqKey() == null ? other.getReqKey() == null : this.getReqKey().equals(other.getReqKey()))
            && (this.getResKey() == null ? other.getResKey() == null : this.getResKey().equals(other.getResKey()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getReqKey() == null) ? 0 : getReqKey().hashCode());
        result = prime * result + ((getResKey() == null) ? 0 : getResKey().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}