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
@FeignClient(value = "pay-service-wx")
@Component
public interface PayOrderService {

    /**
     * 创建支付订单
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/create")
    public String createPayOrder(@RequestParam String jsonParam);

    /**
     * 查询支付订单
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/query")
    public String queryPayOrder(@RequestParam String jsonParam);

    /**
     * 处理微信支付
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/channel/wx")
    public String doWxPayReq(@RequestParam String jsonParam);

    /**
     * 处理支付宝wap支付
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/channel/ali_wap")
    public String doAliPayWapReq(@RequestParam String jsonParam);

    /**
     * 支付宝电脑网站支付
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/channel/ali_pc")
    public String doAliPayPcReq(@RequestParam String jsonParam);

    /**
     * 支付宝手机支付
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/channel/ali_mobile")
    public String doAliPayMobileReq(@RequestParam String jsonParam);

    /**
     * 支付宝当面扫码支付
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "/pay/channel/ali_qr")
    public String doAliPayQrReq(@RequestParam String jsonParam);
}
