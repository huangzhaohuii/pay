package com.copote.alipay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @create 2020/5/14
 * @Description: 微信支付实体类
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeChatEntity {

    /**
     * 公众号id
     */
    private String appid;


    /**
     * 商户号
     */
    private String mchId;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名类型
     */
    private String signType;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 商户订单号
     */
    private String outTradeNo	;

    /**
     * 货币类型
     */
    private String feeType;

    /**
     * 总金额
     */
    private String totalFee;

    /**
     * 终端IP
     */
    private String spbillCreateIp;

    /**
     * 交易起始时间
     */
    private String timeStart;

    /**
     * 交易结束时间
     */
    private String timeExpire;

    /**
     * 商品标记
     */
    private String goodsTag;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 商品ID
     */
    private String productId;

    /**
     * 指定支付方式
     */
    private String limitPay;

    /**
     * 用户标识
     */
    private String openid;

    /**
     * 电子发票入口开放标识
     */
    private String receipt;

    /**
     * 场景信息
     */
    private String sceneInfo;

}
