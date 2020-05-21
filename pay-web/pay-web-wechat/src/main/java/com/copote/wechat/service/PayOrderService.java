package com.copote.wechat.service;

import com.copote.common.exception.R;
import com.copote.wechat.entity.PayOrder;
import com.copote.wechat.entity.WeChatEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

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
     * @param payOrder
     * @return
     */
    @RequestMapping(value = "/pay/create")
    R createPayOrder(@RequestBody PayOrder payOrder);

    /**
     * 查询支付订单
     * @param params
     * @return
     */
    @RequestMapping(value = "/pay/query")
    R queryPayOrder(@RequestBody Map<String,Object> params);

    /**
     * 处理微信支付
     * @param params
     * @return
     */
    @RequestMapping(value = "/pay/channel/wx")
    R doWxPayReq(@RequestBody Map<String,Object> params);

}
