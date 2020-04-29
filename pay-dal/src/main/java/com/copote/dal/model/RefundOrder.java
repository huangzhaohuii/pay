package com.copote.dal.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RefundOrder implements Serializable {
    /**
     * 退款订单号
     *
     * @mbggenerated
     */
    private String refundOrderId;

    /**
     * 支付订单号
     *
     * @mbggenerated
     */
    private String payOrderId;

    /**
     * 渠道支付单号
     *
     * @mbggenerated
     */
    private String channelPayOrderNo;

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    private String mchId;

    /**
     * 商户退款单号
     *
     * @mbggenerated
     */
    private String mchRefundNo;

    /**
     * 渠道ID
     *
     * @mbggenerated
     */
    private String channelId;

    /**
     * 支付金额,单位分
     *
     * @mbggenerated
     */
    private Long payAmount;

    /**
     * 退款金额,单位分
     *
     * @mbggenerated
     */
    private Long refundAmount;

    /**
     * 三位货币代码,人民币:cny
     *
     * @mbggenerated
     */
    private String currency;

    /**
     * 退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败,4-业务处理完成
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 退款结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败
     *
     * @mbggenerated
     */
    private Byte result;

    /**
     * 客户端IP
     *
     * @mbggenerated
     */
    private String clientIp;

    /**
     * 设备
     *
     * @mbggenerated
     */
    private String device;

    /**
     * 备注
     *
     * @mbggenerated
     */
    private String remarkInfo;

    /**
     * 渠道用户标识,如微信openId,支付宝账号
     *
     * @mbggenerated
     */
    private String channelUser;

    /**
     * 用户姓名
     *
     * @mbggenerated
     */
    private String userName;

    /**
     * 渠道商户ID
     *
     * @mbggenerated
     */
    private String channelMchId;

    /**
     * 渠道订单号
     *
     * @mbggenerated
     */
    private String channelOrderNo;

    /**
     * 渠道错误码
     *
     * @mbggenerated
     */
    private String channelErrCode;

    /**
     * 渠道错误描述
     *
     * @mbggenerated
     */
    private String channelErrMsg;

    /**
     * 特定渠道发起时额外参数
     *
     * @mbggenerated
     */
    private String extra;

    /**
     * 通知地址
     *
     * @mbggenerated
     */
    private String notifyUrl;

    /**
     * 扩展参数1
     *
     * @mbggenerated
     */
    private String param1;

    /**
     * 扩展参数2
     *
     * @mbggenerated
     */
    private String param2;

    /**
     * 订单失效时间
     *
     * @mbggenerated
     */
    private Date expireTime;

    /**
     * 订单退款成功时间
     *
     * @mbggenerated
     */
    private Date refundSuccTime;

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

}