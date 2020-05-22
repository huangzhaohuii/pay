package com.copote.wechat.config;

import com.copote.wechat.channel.wechat.WxPayProperties;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @create 2020/5/21
 * @Description: 微信支付配置类
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
//@EnableConfigurationProperties(WxPayProperties.class)
@AllArgsConstructor
public class WeChatConfig {

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxService() {
        WxPayConfig payConfig = new WxPayConfig();
        //APPID
        payConfig.setAppId(StringUtils.trimToNull("wx2a39fb4a8b2c78f4"));
        //商户号
        payConfig.setMchId(StringUtils.trimToNull("1269253701"));
        //商户key
        payConfig.setMchKey(StringUtils.trimToNull("098f6bcd4621d373cade4e832627b4f6"));
        //密钥路径
        payConfig.setKeyPath(StringUtils.trimToNull("d:/apiclient_cert.p12"));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
