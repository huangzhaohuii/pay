package com.copote.wechat.service.impl;

import com.copote.wechat.service.WeChatOrderService;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayQueryCommentRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayReportRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.*;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Administrator
 * @create 2020/5/21
 * @Description:
 * @since 1.0.0
 */
@Service("weChatOrderService")
public class WeChatOrderServiceImpl implements WeChatOrderService {

    @Autowired
    private WxPayService wxPayService;


    @Override
    public WxPayUnifiedOrderResult unifiedOrder(WxPayUnifiedOrderRequest request) throws WxPayException {
        return wxPayService.unifiedOrder(request);
    }

    @Override
    public WxPayOrderCloseResult closeOrder(String outTradeNo) throws WxPayException {
        return wxPayService.closeOrder(outTradeNo);
    }

    @Override
    public WxPayRefundResult refund(WxPayRefundRequest request) throws WxPayException {
        return wxPayService.refund(request);
    }

    @Override
    public WxPayRefundQueryResult refundQuery(String transactionId, String outTradeNo, String outRefundNo, String refundId) throws WxPayException {
        return wxPayService.refundQuery(transactionId,outTradeNo,outRefundNo,refundId);
    }

    @Override
    public WxPayBillResult downloadRawBill(String billDate, String billType, String tarType, String deviceInfo) throws WxPayException {
        return wxPayService.downloadBill(billDate,billType,tarType,deviceInfo);
    }

    @Override
    public WxPayOrderNotifyResult parseOrderNotifyResult(String xmlData) throws WxPayException {
        return wxPayService.parseOrderNotifyResult(xmlData);
    }

    @Override
    public void report(WxPayReportRequest request) throws WxPayException {
        wxPayService.report(request);
    }

    @Override
    public WxPayRefundNotifyResult parseRefundNotifyResult(String xmlData) throws WxPayException {
        return wxPayService.parseRefundNotifyResult(xmlData);
    }

    @Override
    public String queryComment(Date beginDate, Date endDate, Integer offset, Integer limit) throws WxPayException {
        return wxPayService.queryComment(beginDate,endDate,offset,limit);
    }
}
