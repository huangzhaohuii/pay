package com.copote.alipay.service;

import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @create 2020/5/11
 * @Description:
 * @since 1.0.0
 */
@Component
public class MchInfoFallBackService implements MchInfoService{


    @Override
    public String selectMchInfo(String jsonParam) {
        return "服务降级，系统错误";
    }
}
