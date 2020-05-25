package com.copote.wechat.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.copote.common.constant.PayConstant;
import com.copote.common.constant.PayEnum;
import com.copote.common.exception.R;
import com.copote.common.util.MyBase64;
import com.copote.common.util.MyLog;
import com.copote.common.util.XXPayUtil;
import com.copote.wechat.entity.MchInfo;
import com.copote.wechat.entity.PayChannel;
import com.copote.wechat.entity.PayOrder;
import com.copote.wechat.service.MchInfoService;
import com.copote.wechat.service.PayChannelService;
import com.copote.wechat.service.PayOrderService;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 支付渠道接口:微信
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
public class PayChannel4WxController{

    private final MyLog _log = MyLog.getLog(PayChannel4WxController.class);

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private MchInfoService mchInfoService;


    /**
     * 发起微信支付(统一下单)
     * @param
     * @return
     */
    @RequestMapping(value = "/pay/channel/wx")
    public R doWxPayReq(@RequestBody Map<String,Object> params) {
        try{
            PayOrder payOrder = (PayOrder) params.get("payOrder");
            String tradeType = (String) params.get("tradeType");
            String logPrefix = "【微信支付统一下单】";
            String mchId = payOrder.getMchId();
            String channelId = payOrder.getChannelId();
            MchInfo mchInfo = mchInfoService.getById(mchId);
            String resKey = mchInfo == null ? "" : mchInfo.getResKey();
            if(StrUtil.isEmpty(resKey)) {
                return R.error("支付密钥为空");
            }
            //查询渠道
            PayChannel payChannel = payChannelService.selectPayChannel(channelId);
            //获取微信配置
            WxPayConfig wxPayConfig = null;
//            WxPayUtil.getWxPayConfig(payChannel.getParam(), tradeType, wxPayProperties.getCertRootPath(), wxPayProperties.getNotifyUrl());
            //微信服务
            WxPayService wxPayService = new WxPayServiceImpl();
//            wxPayService.setConfig(wxPayConfig);
            //构建微信统一下单请求数据
            WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = buildUnifiedOrderRequest(payOrder, wxPayConfig);
            String payOrderId = payOrder.getPayOrderId();
            WxPayUnifiedOrderResult wxPayUnifiedOrderResult;
            try {
                //下单
                wxPayUnifiedOrderResult = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
                _log.info("{} >>> 下单成功", logPrefix);
                Map<String, Object> map = XXPayUtil.makeRetMap(PayConstant.RETURN_VALUE_SUCCESS, "", PayConstant.RETURN_VALUE_SUCCESS, null);
                map.put("payOrderId", payOrderId);
                map.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
                //更新支付状态为正在支付
                int result = payOrderService.updateStatus4Ing(payOrderId, wxPayUnifiedOrderResult.getPrepayId());
                _log.info("更新第三方支付订单号:payOrderId={},prepayId={},result={}", payOrderId, wxPayUnifiedOrderResult.getPrepayId(), result);
                switch (tradeType) {
                    //二维码支付
                    case PayConstant.WxConstant.TRADE_TYPE_NATIVE : {
                        // 二维码支付链接
                        map.put("codeUrl", wxPayUnifiedOrderResult.getCodeURL());
                        break;
                    }
                    //APP支付
                    case PayConstant.WxConstant.TRADE_TYPE_APP : {
                        Map<String, String> payInfo = new HashMap<>();
                        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                        String nonceStr = String.valueOf(System.currentTimeMillis());
                        // APP支付绑定的是微信开放平台上的账号，APPID为开放平台上绑定APP后发放的参数
                        String appId = wxPayConfig.getAppId();
                        Map<String, String> configMap = new HashMap<>();
                        // 此map用于参与调起sdk支付的二次签名,格式全小写，timestamp只能是10位,格式固定，切勿修改
                        String partnerId = wxPayConfig.getMchId();
                        configMap.put("prepayid", wxPayUnifiedOrderResult.getPrepayId());
                        configMap.put("partnerid", partnerId);
                        String packageValue = "Sign=WXPay";
                        configMap.put("package", packageValue);
                        configMap.put("timestamp", timestamp);
                        configMap.put("noncestr", nonceStr);
                        configMap.put("appid", appId);
                        // 此map用于客户端与微信服务器交互
                        payInfo.put("sign", SignUtils.createSign(configMap, wxPayConfig.getMchKey(), null));
                        payInfo.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
                        payInfo.put("partnerId", partnerId);
                        payInfo.put("appId", appId);
                        payInfo.put("packageValue", packageValue);
                        payInfo.put("timeStamp", timestamp);
                        payInfo.put("nonceStr", nonceStr);
                        map.put("payParams", payInfo);
                        break;
                    }
                    //JSAPI支付
                    case PayConstant.WxConstant.TRADE_TYPE_JSPAI : {
                        Map<String, String> payInfo = new HashMap<>();
                        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                        String nonceStr = String.valueOf(System.currentTimeMillis());
                        payInfo.put("appId", wxPayUnifiedOrderResult.getAppid());
                        // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                        payInfo.put("timeStamp", timestamp);
                        payInfo.put("nonceStr", nonceStr);
                        payInfo.put("package", "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId());
                        payInfo.put("signType", WxPayConstants.SignType.MD5);
                        payInfo.put("paySign", SignUtils.createSign(payInfo, wxPayConfig.getMchKey(), null));
                        map.put("payParams", payInfo);
                        break;
                    }
                    //H5支付
                    case PayConstant.WxConstant.TRADE_TYPE_MWEB : {
                        // h5支付链接地址
                        map.put("payUrl", wxPayUnifiedOrderResult.getMwebUrl());
                        break;
                    }
                    default:{

                    }

                }
                String res = XXPayUtil.makeRetData(map, resKey);
                return R.ok().put("res",res);
            } catch (WxPayException e) {
                _log.error(e, "下单失败");
                //出现业务错误
                _log.info("{}下单返回失败", logPrefix);
                _log.info("err_code:{}", e.getErrCode());
                _log.info("err_code_des:{}", e.getErrCodeDes());
                return R.error("下单返回失败");
            }
        }catch (Exception e) {
            _log.error(e, "微信支付统一下单异常");
            return R.error("微信支付统一下单异常");
        }
    }

    /**
     * 构建微信统一下单请求数据
     * @param payOrder
     * @param wxPayConfig
     * @return
     */
    WxPayUnifiedOrderRequest buildUnifiedOrderRequest(PayOrder payOrder, WxPayConfig wxPayConfig) {
        String tradeType = wxPayConfig.getTradeType();
        String payOrderId = payOrder.getPayOrderId();
        // 支付金额,单位分
        Integer totalFee = payOrder.getAmount().intValue();
        //设备
        String deviceInfo = payOrder.getDevice();
        String body = payOrder.getBody();
        String detail = null;
        String attach = null;
        String outTradeNo = payOrderId;
        String feeType = "CNY";
        String spBillCreateIP = payOrder.getClientIp();
        String timeStart = null;
        String timeExpire = null;
        String goodsTag = null;
        String notifyUrl = wxPayConfig.getNotifyUrl();
        String productId = null;
        if(tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_NATIVE)){
            productId = JSON.parseObject(payOrder.getExtra()).getString("productId");
        }
        String limitPay = null;
        String openId = null;
        if(tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_JSPAI)){
            openId = JSON.parseObject(payOrder.getExtra()).getString("openId");
        }
        String sceneInfo = null;
        if(tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_MWEB)){
            sceneInfo = JSON.parseObject(payOrder.getExtra()).getString("sceneInfo");
        }
        // 微信统一下单请求对象
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setDeviceInfo(deviceInfo);
        request.setBody(body);
        request.setDetail(detail);
        request.setAttach(attach);
        request.setOutTradeNo(outTradeNo);
        request.setFeeType(feeType);
        request.setTotalFee(totalFee);
        request.setSpbillCreateIp(spBillCreateIP);
        request.setTimeStart(timeStart);
        request.setTimeExpire(timeExpire);
        request.setGoodsTag(goodsTag);
        request.setNotifyURL(notifyUrl);
        request.setTradeType(tradeType);
        request.setProductId(productId);
        request.setLimitPay(limitPay);
        request.setOpenid(openId);
        request.setSceneInfo(sceneInfo);
        return request;
    }
}
