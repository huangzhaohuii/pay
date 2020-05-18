package com.copote.wechat.service;

import com.copote.common.exception.R;
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
@Component
@FeignClient(value = "pay-service-wx")
public interface PayChannelService {

    /**
     * 渠道查询
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay_channel/select")
    public R selectPayChannel(@RequestParam String jsonParam);
}
