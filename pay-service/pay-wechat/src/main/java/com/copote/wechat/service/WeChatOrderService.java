package com.copote.wechat.service;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayReportRequest;
import com.github.binarywang.wxpay.bean.result.WxPayBillResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;

import java.util.Date;

/**
 * @author Administrator
 * @create 2020/5/21
 * @Description:
 * @since 1.0.0
 */
public interface WeChatOrderService {
    /**
     * 关闭订单
     *  应用场景
     *  以下情况需要调用关单接口：
     *  1. 商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
     *  2. 系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
     *  注意：订单生成后不能马上调用关单接口，最短调用时间间隔为5分钟。
     *  接口地址：https://api.mch.weixin.qq.com/pay/closeorder
     *  是否需要证书：   不需要。
     * @param outTradeNo 商户系统内部的订单号
     * @return
     * @throws WxPayException
     */
    WxPayOrderCloseResult closeOrder(String outTradeNo) throws WxPayException;

    /**
     * 微信支付-申请退款.
     * 详见 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_4
     * 接口链接：https://api.mch.weixin.qq.com/secapi/pay/refund
     * @param request
     * @return
     * @throws WxPayException
     */
    WxPayRefundResult refund(WxPayRefundRequest request) throws WxPayException;

    /**
     * 查询退款
     * @param transactionId
     * @param outTradeNo
     * @param outRefundNo
     * @param refundId
     * @return
     * @throws WxPayException
     */
    WxPayRefundQueryResult refundQuery(String transactionId, String outTradeNo, String outRefundNo, String refundId) throws WxPayException;

    /**
     * 下载对账单
     * @param billDate
     * @param billType
     * @param tarType
     * @param deviceInfo
     * @return
     */
    WxPayBillResult downloadRawBill(String billDate, String billType, String tarType, String deviceInfo) throws WxPayException;

    /**
     * 解析支付结果通知
     * @param xmlData
     * @return
     * @throws WxPayException
     */
    WxPayOrderNotifyResult parseOrderNotifyResult(String xmlData) throws WxPayException;

    /**
     * 交易保障
     * @param request
     * @throws WxPayException
     */
    void report(WxPayReportRequest request) throws WxPayException;

    /**
     * 解析退款结果通知
     * @param xmlData
     * @return
     * @throws WxPayException
     */
    WxPayRefundNotifyResult parseRefundNotifyResult(String xmlData) throws WxPayException;


    /**
     * 拉去订单评价数据
     * @param beginDate
     * @param endDate
     * @param offset
     * @param limit
     * @return
     * @throws WxPayException
     */
    String	queryComment(Date beginDate, Date endDate, Integer offset, Integer limit) throws WxPayException;


}
