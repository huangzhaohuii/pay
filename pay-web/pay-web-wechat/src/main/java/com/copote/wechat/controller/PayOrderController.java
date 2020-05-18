package com.copote.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.copote.common.constant.PayConstant;
import com.copote.common.exception.R;
import com.copote.common.util.JsonUtil;
import com.copote.common.util.MySeq;
import com.copote.common.util.RUtil;
import com.copote.common.util.XXPayUtil;
import com.copote.common.validator.ValidatorUtils;
import com.copote.wechat.entity.WeChatEntity;
import com.copote.wechat.service.MchInfoService;
import com.copote.wechat.service.PayChannelService;
import com.copote.wechat.service.PayOrderService;
import com.copote.wechat.util.ValidateWeChatParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @create 2020/5/14
 * @Description: 微信订单支付,包括:统一下单,订单查询,补单等接口
 * @since 1.0.0
 */
@RestController
@Slf4j
@RequestMapping(value = "wechat/pay")
public class PayOrderController {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private MchInfoService mchInfoService;

    @Autowired
    private ValidateWeChatParamsUtil validateWeChatParamsUtil;

    /**
     * 统一下单接口:
     * 1)先验证接口参数以及签名信息
     * 2)验证通过创建支付订单
     * 3)根据商户选择渠道,调用支付服务进行下单
     * 4)返回下单数据
     * @param weChatEntity
     * @return
     */
    @RequestMapping(value = "/create_order")
    public R payOrder(@RequestBody WeChatEntity weChatEntity) {
        log.info("###### 开始接收w微信统一下单请求 ######");
        String logPrefix = "【商户统一下单】";
        try {
            JSONObject payOrder = null;
            //先检验参数的有效性
            ValidatorUtils.validateEntity(weChatEntity);
            R r = validateWeChatParamsUtil.validateParams(weChatEntity);
            if(RUtil.checkErro(r)){
                return r;
            }
            r = payOrderService.createPayOrder(weChatEntity);
            if(RUtil.checkErro(r)){
                return R.error("创建微信支付订单失败");
            }
            log.info("{}创建支付订单失败", logPrefix);
            String channelId = payOrder.getString("channelId");
            switch (channelId) {
                case PayConstant.PAY_CHANNEL_WX_APP :
                    return payOrderService.doWxPayReq(JsonUtil.getJsonParam(new String[]{"tradeType", "payOrder"}, new Object[]{PayConstant.WxConstant.TRADE_TYPE_APP, weChatEntity}));
                case PayConstant.PAY_CHANNEL_WX_JSAPI :
                    return payOrderService.doWxPayReq(JsonUtil.getJsonParam(new String[]{"tradeType", "payOrder"}, new Object[]{PayConstant.WxConstant.TRADE_TYPE_JSPAI, weChatEntity}));
                case PayConstant.PAY_CHANNEL_WX_NATIVE :
                    return payOrderService.doWxPayReq(JsonUtil.getJsonParam(new String[]{"tradeType", "payOrder"}, new Object[]{PayConstant.WxConstant.TRADE_TYPE_NATIVE, weChatEntity}));
                case PayConstant.PAY_CHANNEL_WX_MWEB :
                    return payOrderService.doWxPayReq(JsonUtil.getJsonParam(new String[]{"tradeType", "payOrder"}, new Object[]{PayConstant.WxConstant.TRADE_TYPE_MWEB, weChatEntity}));
                default:
                    return R.error("微信支付渠道错误");
            }
        }catch (Exception e) {
            log.error((Marker) e, "微信支付中心异常");
            return R.error("微信支付中心异常");
        }
    }

}
