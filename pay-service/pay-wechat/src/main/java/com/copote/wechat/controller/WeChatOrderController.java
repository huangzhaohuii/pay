package com.copote.wechat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.copote.common.constant.PayConstant;
import com.copote.common.exception.R;
import com.copote.wechat.entity.PayOrder;
import com.copote.wechat.service.PayOrderService;
import com.copote.wechat.service.WeChatOrderService;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayBillResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(outTradeNo);
        payOrder.setStatus(PayConstant.TRADE_STATUS_CLOSED);
        //更新订单状态为已关闭
        payOrderService.updateById(payOrder);
        return R.ok();
    }

    /**
     * 申请退款
     * @return
     */
    @RequestMapping("/refund")
    public R refund(@RequestBody Map<String,Object> params) throws WxPayException {
        //订单号
        String orderId = (String) params.get("payOrderId");
        //退款金额
        Integer refundFee = (Integer) params.get("refundFee");
        //退款原因
        String refundDesc = (String) params.get("refundDesc");
        String time = DateUtil.format(new Date(),"yyyyMMdd");
        PayOrder order = payOrderService.getById(orderId);
        if(BeanUtil.isEmpty(order)){
            return R.error("订单不存在："+orderId);
        }
        WxPayRefundRequest refundRequest = new WxPayRefundRequest();
        //设备信息
        refundRequest.setDeviceInfo(order.getDevice());
        //微信订单号(二选一)
        refundRequest.setTransactionId("");
        //商户订单号
        refundRequest.setOutTradeNo(order.getMchOrderNo());
        //商户退款订单号，商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
        refundRequest.setOutRefundNo("tf"+ time + order.getMchOrderNo());
        //订单金额
        refundRequest.setTotalFee(order.getAmount().intValue());
        //退款金额
        refundRequest.setRefundFee(refundFee);
        //退款资金来源
        /*仅针对老资金流商户使用
        REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
        REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款*/
        refundRequest.setRefundAccount("REFUND_SOURCE_UNSETTLED_FUNDS");
        //退款原因
        refundRequest.setRefundDesc(refundDesc);
        try{
            WxPayRefundResult result = weChatOrderService.refund(refundRequest);
            if(result.getResultCode().equals("FAIL")){
                return R.error("退款失败！"+result.getErrCodeDes());
            }
        }catch (Exception e){
            log.info("退款失败");
            return R.error("退款失败");
        }
        PayOrder refundOrder = new PayOrder();
        refundOrder.setPayOrderId(orderId);
        refundOrder.setStatus(PayConstant.TRADE_STATUS_REFUNDED);
        //更新订单退款状态
        payOrderService.updateById(refundOrder);
        return R.ok();
    }

    /**
     * 查询退款
     * 微信支付-查询退款.
     * 应用场景：
     * 提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，
     * 银行卡支付的退款3个工作日后重新查询退款状态。
     * 详见 https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_5
     * 接口链接：https://api.mch.weixin.qq.com/pay/refundquery
     * 参数：
     * transactionId - 微信订单号
     * outTradeNo - 商户订单号
     * outRefundNo - 商户退款单号
     * refundId - 微信退款单号
     * @return
     */
    @RequestMapping("queryRefund")
    public R queryRefund(@RequestBody Map<String,Object> params) throws WxPayException {
        //微信订单号
        String transactionId = (String) params.get("transactionId");
        //商户订单号
        String outTradeNo = (String) params.get("outTradeNo");
        //商户退款单号
        String outRefundNo = (String) params.get("outRefundNo");
        //微信退款单号
        String refundId = (String) params.get("refundId");
        //偏移量（当部分退款次数超过10次时可使用，表示返回的查询结果从这个偏移量开始取记录）
        String offset = (String) params.get("offset");
        WxPayRefundQueryResult result = weChatOrderService.refundQuery(transactionId, outTradeNo, outRefundNo, refundId);
        if(result.getReturnCode().equals(PayConstant.RETURN_VALUE_FAIL)){
            return R.error("查询退款失败！"+result.getReturnMsg()+"!");
        }
        if(result.getResultCode().equals(PayConstant.RESULT_VALUE_SUCCESS)){
            return R.error("查询退款失败！"+result.getErrCodeDes() +"!");
        }
        return R.ok();
    }


    /**
     * 下载对账单.
     *  商户可以通过该接口下载历史交易清单。比如掉单、系统错误等导致商户侧和微信侧数据不一致，通过对账单核对后可校正支付状态。
     *  注意：
     *  1、微信侧未成功下单的交易不会出现在对账单中。支付成功后撤销的交易会出现在对账单中，跟原支付单订单号一致，bill_type为REVOKED；
     *  2、微信在次日9点启动生成前一天的对账单，建议商户10点后再获取；
     *  3、对账单中涉及金额的字段单位为“元”。
     *  4、对账单接口只能下载三个月以内的账单。
     *  接口链接：https://api.mch.weixin.qq.com/pay/downloadbill
     *  详情请见: 下载对账单
     *  billDate - 对账单日期 bill_date 下载对账单的日期，格式：20140603
     *  billType - 账单类型 bill_type ALL，返回当日所有订单信息，默认值，SUCCESS，返回当日成功支付的订单，REFUND，返回当日退款订单
     *  tarType - 压缩账单 tar_type 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
     *  deviceInfo - 设备号 device_info 非必传参数，终端设备号
     * @param params
     * @return
     * @throws WxPayException
     */
    @RequestMapping("downloadBill")
    public R downloadBill(@RequestBody Map<String,Object> params)throws WxPayException{
        //对账单日期
        String billDate = (String) params.get("billDate ");
        //账单类型
        String billType = (String) params.get("billType");
        //压缩账单
        String tarType = (String) params.get("tarType");
        WxPayBillResult result = weChatOrderService.downloadRawBill(billDate, billType, tarType, "");
        return R.ok();
    }


    /**
     * 下载资金账单.
     *  商户可以通过该接口下载自2017年6月1日起 的历史资金流水账单。
     *  注意：
     *  1、资金账单中的数据反映的是商户微信账户资金变动情况；
     *  2、当日账单在次日上午9点开始生成，建议商户在上午10点以后获取；
     *  3、资金账单中涉及金额的字段单位为“元”。
     *  接口链接：https://api.mch.weixin.qq.com/pay/downloadfundflow
     *  详情请见: 下载对账单
     *  billDate - 资金账单日期 bill_date 下载对账单的日期，格式：20140603
     * accountType - 资金账户类型 account_type Basic，基本账户，Operation，运营账户，Fees，手续费账户
     * tarType - 压缩账单 tar_type 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
     * @param params
     * @return
     * @throws WxPayException
     */
    public R downloadFundFlow(@RequestBody Map<String,Object> params)throws WxPayException{
        //对账日期
        String billDate = (String) params.get("billDate");
        //资金账户类型
        String accountType = (String) params.get("accountType");
        //压缩账单
        String tarType = (String) params.get("tarType");
        return R.ok();
    }



}
