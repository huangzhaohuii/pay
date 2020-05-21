package com.copote.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.copote.common.constant.PayConstant;
import com.copote.common.exception.R;
import com.copote.common.util.JsonUtil;
import com.copote.common.util.MapUtils;
import com.copote.common.util.RUtil;
import com.copote.common.util.XXPayUtil;
import com.copote.wechat.service.MchInfoService;
import com.copote.wechat.service.PayOrderService;
import com.copote.wechat.util.ValidateWeChatParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description: 支付订单查询
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-08-31
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
@Slf4j
@RequestMapping(value = "wechat/pay")
public class QueryPayOrderController {


    @Autowired
    private DiscoveryClient client;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private ValidateWeChatParamsUtil validateWeChatParamsUtil;

    /**
     * 查询支付订单接口:
     * 1)先验证接口参数以及签名信息
     * 2)根据参数查询订单
     * 3)返回订单数据
     * @param params
     * @return
     */
    @RequestMapping(value = "/query")
    public R queryPayOrder(@RequestBody Map<String,String> params) {
        log.info("###### 开始接收商户查询支付订单请求 ######");
        String logPrefix = "【商户支付订单查询】";
        try {
            //校验参数
            R r = validateWeChatParamsUtil.validateQueryParams(params);
            if(RUtil.checkErro(r)){
                return r;
            }
            String reqKey = (String) ((JSONObject)r.get("data")).get("reqKey");
            log.debug("请求参数及签名校验通过");
            // 商户ID
            String mchId = params.get("mchId");
            // 商户订单号
            String mchOrderNo = params.get("mchOrderNo");
            // 支付订单号
            String payOrderId = params.get("payOrderId");
            // 是否执行回调
            String executeNotify = params.get("executeNotify");
            JSONObject payOrder;
            r = payOrderService.queryPayOrder(MapUtils.getMapParams(new String[]{"mchId", "payOrderId", "mchOrderNo", "executeNotify"}, new Object[]{mchId, payOrderId, mchOrderNo, executeNotify}));
            if(RUtil.checkErro(r)) {
                log.info("{}查询支付订单失败", logPrefix);
                return R.error("查询支付订单失败");
            }
            Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
            JSONObject data = (JSONObject) r.get("data");
            log.info("商户查询订单成功,payOrder={}", data);
            log.info("###### 商户查询订单处理完成 ######");
            return R.ok().put("data",XXPayUtil.makeRetData(map, reqKey));
        }catch (Exception e) {
            log.error("支付系统系统错误");
            return R.error("支付系统系统错误");
        }
    }

}
