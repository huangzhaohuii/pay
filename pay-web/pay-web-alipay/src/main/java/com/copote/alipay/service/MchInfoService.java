package com.copote.alipay.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Administrator
 * @create 2020/5/11
 * @Description:
 * @since 1.0.0
 */
@FeignClient(value = "pay-service-wx",fallback = MchInfoFallBackService.class)
@Component
public interface MchInfoService {

    /**
     * 查询商户
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/mch_info/select")
    public String selectMchInfo(@RequestParam String jsonParam);
}
