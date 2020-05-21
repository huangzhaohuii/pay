package com.copote.wechat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.copote.common.exception.R;
import com.copote.wechat.entity.PayOrder;
import com.copote.wechat.service.PayOrderService;
import com.copote.wechat.service.WeChatOrderService;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Administrator
 * @create 2020/5/21
 * @Description: 微信支付订单操作
 * @since 1.0.0
 */
@RestController
@Slf4j
@RequestMapping("pay/weChat")
public class WeChatOrderController {

    @Autowired
    private WeChatOrderService weChatOrderService;

    @Autowired
    private PayOrderService payOrderService;

    /**
     * 关闭订单
     * @return
     */
    @RequestMapping("/closeOrder/{outTradeNo}")
    public R closeOrder(@PathVariable String outTradeNo) throws WxPayException {
        WxPayOrderCloseResult result = weChatOrderService.closeOrder(outTradeNo);
        if(StrUtil.equals(result.getResultCode(),"FAIL")){
            return R.error("关闭订单失败,"+result.getErrCodeDes()+"！请五分钟后再试！");
        }
        return R.ok();
    }

    /**
     * 申请退款
     * @return
     */
    @RequestMapping("/refund")
    public R refund(@RequestBody Map<String,Object> params){
        String orderId = (String) params.get("orderid");
        PayOrder order = payOrderService.getById(orderId);
        if(BeanUtil.isEmpty(order)){
            return R.error("订单不存在："+orderId);
        }
        WxPayRefundRequest refundRequest = new WxPayRefundRequest();
        refundRequest.setDeviceInfo(order.getDevice());
        //微信订单号(二选一)
        refundRequest.setTransactionId("");
        //商户订单号
        refundRequest.setOutTradeNo(order.getMchOrderNo());
        //商户退款订单号，商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
        refundRequest.setOutRefundNo("tf"+order.getMchOrderNo());
        //订单金额
        refundRequest.setTotalFee(order.getAmount().intValue());
        refundRequest.setRefundFee(1);
        //退款资金来源
        /*仅针对老资金流商户使用
        REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
        REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款*/
        refundRequest.setRefundAccount("REFUND_SOURCE_UNSETTLED_FUNDS");
        //退款原因
        refundRequest.setRefundDesc("");
        //随机字符串
        refundRequest.setNonceStr("");
        //签名
        refundRequest.setSign("");
        System.out.println(refundRequest);
        return R.ok();
    }



}
