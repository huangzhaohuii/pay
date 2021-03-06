package com.copote.wechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.copote.common.constant.PayConstant;
import com.copote.common.util.MyLog;
import com.copote.common.util.PayDigestUtil;
import com.copote.common.util.XXPayUtil;
import com.copote.wechat.entity.MchInfo;
import com.copote.wechat.entity.PayOrder;
import com.copote.wechat.service.MchInfoService;
import com.copote.wechat.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 支付通知处理基类
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class Notify4BasePay {

	private static final MyLog _log = MyLog.getLog(Notify4BasePay.class);

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private MchInfoService mchInfoService;

	/**
	 * 创建响应URL及参数
	 * @param payOrder
	 * @param backType 1：前台页面；2：后台接口
	 * @return
	 */
	public String createNotifyUrl(PayOrder payOrder, String backType) {
		String mchId = payOrder.getMchId();
		MchInfo mchInfo = mchInfoService.getById(mchId);
		String resKey = mchInfo.getResKey();
		Map<String, Object> paramMap = new HashMap<>();
		// 支付订单号
		paramMap.put("payOrderId", payOrder.getPayOrderId() == null ? "" : payOrder.getPayOrderId());
		// 商户ID
		paramMap.put("mchId", payOrder.getMchId() == null ? "" : payOrder.getMchId());
		// 商户订单号
		paramMap.put("mchOrderNo", payOrder.getMchOrderNo() == null ? "" : payOrder.getMchOrderNo());
		// 渠道ID
		paramMap.put("channelId", payOrder.getChannelId() == null ? "" : payOrder.getChannelId());
		// 支付金额
		paramMap.put("amount", payOrder.getAmount() == null ? "" : payOrder.getAmount());
		// 货币类型
		paramMap.put("currency", payOrder.getCurrency() == null ? "" : payOrder.getCurrency());
		// 支付状态
		paramMap.put("status", payOrder.getStatus() == null ? "" : payOrder.getStatus());
		// 客户端IP
		paramMap.put("clientIp", payOrder.getClientIp()==null ? "" : payOrder.getClientIp());
		// 设备
		paramMap.put("device", payOrder.getDevice()==null ? "" : payOrder.getDevice());
		// 商品标题
		paramMap.put("subject", payOrder.getSubject()==null ? "" : payOrder.getSubject());
		// 渠道订单号
		paramMap.put("channelOrderNo", payOrder.getChannelOrderNo()==null ? "" : payOrder.getChannelOrderNo());
		// 扩展参数1
		paramMap.put("param1", payOrder.getParam1()==null ? "" : payOrder.getParam1());
		// 扩展参数2
		paramMap.put("param2", payOrder.getParam2()==null ? "" : payOrder.getParam2());
		// 支付成功时间
		paramMap.put("paySuccTime", payOrder.getPaySuccTime()==null ? "" : payOrder.getPaySuccTime());
		paramMap.put("backType", backType==null ? "" : backType);
		// 先对原文签名
		String reqSign = PayDigestUtil.getSign(paramMap, resKey);
		// 签名
		paramMap.put("sign", reqSign);
		// 签名后再对有中文参数编码
		try {
			paramMap.put("device", URLEncoder.encode(payOrder.getDevice()==null ? "" : payOrder.getDevice(), PayConstant.RESP_UTF8));
			paramMap.put("subject", URLEncoder.encode(payOrder.getSubject()==null ? "" : payOrder.getSubject(), PayConstant.RESP_UTF8));
			paramMap.put("param1", URLEncoder.encode(payOrder.getParam1()==null ? "" : payOrder.getParam1(), PayConstant.RESP_UTF8));
			paramMap.put("param2", URLEncoder.encode(payOrder.getParam2()==null ? "" : payOrder.getParam2(), PayConstant.RESP_UTF8));
		}catch (UnsupportedEncodingException e) {
			_log.error("URL Encode exception.", e);
			return null;
		}
		String param = XXPayUtil.genUrlParams(paramMap);
		StringBuffer sb = new StringBuffer();
		sb.append(payOrder.getNotifyUrl()).append("?").append(param);
		return sb.toString();
	}

	/**
	 * 处理支付结果前台页面跳转
	 */
	public boolean doPage(PayOrder payOrder, HttpServletResponse response) {
		String redirectUrl = createNotifyUrl(payOrder, "1");
		_log.info("redirect to respUrl:"+redirectUrl);
		// 前台跳转业务系统
		try {
			response.sendRedirect(redirectUrl);
		} catch (IOException e) {
			_log.error("XxPay sendRedirect exception. respUrl="+redirectUrl, e);
			return false;
		}
		return true;
	}

	/**
	 * 处理支付结果后台服务器通知
	 */
	public void doNotify(PayOrder payOrder) {
		_log.info(">>>>>> PAY开始回调通知业务系统 <<<<<<");
		// 发起后台通知业务系统
		JSONObject object = createNotifyInfo(payOrder);
		try {
            notifyService.send(object.toJSONString());
		} catch (Exception e) {
			_log.error("payOrderId={},sendMessage error.", payOrder != null ? payOrder.getPayOrderId() : "", e);
		}
		_log.info(">>>>>> PAY回调通知业务系统完成 <<<<<<");
	}

	public JSONObject createNotifyInfo(PayOrder payOrder) {
		JSONObject object = new JSONObject();
		object.put("method", "GET");
		object.put("url", createNotifyUrl(payOrder, "2"));
		object.put("orderId", payOrder.getPayOrderId());
		object.put("count", payOrder.getNotifyCount());
		object.put("createTime", System.currentTimeMillis());
		return object;
	}

}
