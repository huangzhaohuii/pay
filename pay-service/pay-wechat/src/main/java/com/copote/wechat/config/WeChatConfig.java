package com.copote.wechat.config;

import com.copote.wechat.properties.WxPayProperties;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@EnableConfigurationProperties(WxPayProperties.class)
@AllArgsConstructor
public class WeChatConfig {

    @Autowired
    private WxPayProperties wxPayProperties;

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxService() {
        WxPayConfig payConfig = new WxPayConfig();
        //APPID
        payConfig.setAppId(StringUtils.trimToNull(wxPayProperties.getAppId()));
        //商户号
        payConfig.setMchId(StringUtils.trimToNull(wxPayProperties.getMchId()));
        //商户key
        payConfig.setMchKey(StringUtils.trimToNull(wxPayProperties.getMchKey()));
        //密钥路径
        payConfig.setKeyPath(StringUtils.trimToNull(wxPayProperties.getKeyPath()));

        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
