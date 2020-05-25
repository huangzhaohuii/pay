package com.copote.wechat.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @create 2020/5/25
 * @Description: 微信配置类
 * @since 1.0.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "wechat")
public class WxPayProperties {

    /**
     * APPID
     */
//    @Value("${wechat.appId}")
    private String appId;

    /**
     * 商户号
     */
//    @Value("${wechat.mchId}")
    private String mchId;

    /**
     * 商户key
     */
//    @Value("${wechat.mchKey}")
    private String mchKey;

    /**
     * 密钥路径
     */
//    @Value("${wechat.keyPath}")
    private String keyPath;

}
