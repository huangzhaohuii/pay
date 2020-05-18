package com.copote.wechat.service;

import com.copote.common.exception.R;
import com.copote.wechat.entity.WeChatEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Administrator
 * @create 2020/5/11
 * @Description:
 * @since 1.0.0
 */
@FeignClient(value = "pay-service-wx")
@Component
public interface PayOrderService {

    /**
     * 创建支付订单
     * @param weChatEntity
     * @return
     */
    @RequestMapping(value = "/pay/create")
    public R createPayOrder(@RequestBody WeChatEntity weChatEntity);

    /**
     * 查询支付订单
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/query")
    public R queryPayOrder(@RequestParam String jsonParam);

    /**
     * 处理微信支付
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/channel/wx")
    public R doWxPayReq(@RequestParam String jsonParam);

}
