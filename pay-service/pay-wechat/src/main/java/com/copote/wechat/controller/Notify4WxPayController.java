package com.copote.wechat.controller;

import cn.hutool.core.util.ObjectUtil;
import com.copote.common.constant.PayConstant;
import com.copote.common.util.MyLog;
import com.copote.wechat.channel.wechat.WxPayUtil;
import com.copote.wechat.entity.Customer;
import com.copote.wechat.entity.PayChannel;
import com.copote.wechat.entity.PayOrder;
import com.copote.wechat.service.CustomerService;
import com.copote.wechat.service.PayChannelService;
import com.copote.wechat.service.PayOrderService;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @create 2020/5/11
 * @Description: 接收处理微信通知(微信回调)
 * @since 1.0.0
 */
@RestController
@Slf4j
public class Notify4WxPayController extends Notify4BasePay {

	private static final MyLog _log = MyLog.getLog(Notify4WxPayController.class);

	@Autowired
	private PayOrderService payOrderService;

	@Autowired
	private PayChannelService payChannelService;

	@Autowired
	private CustomerService customerService;

	/**
	 * 微信支付(统一下单接口)后台通知响应
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
     */
	@RequestMapping("/pay/wxPayNotifyRes.htm")
	@ResponseBody
	public String wxPayNotifyRes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return doWxPayRes(request, response);
	}

	public String doWxPayRes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String logPrefix = "【微信支付回调通知】";
		_log.info("====== 开始接收微信支付回调通知 ======");
		try {
			String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
			WxPayService wxPayService = new WxPayServiceImpl();
			WxPayOrderNotifyResult result = WxPayOrderNotifyResult.fromXML(xmlResult);
			Map<String, Object> payContext = new HashMap();
			payContext.put("parameters", result);
			// 验证业务数据是否正确,验证通过后返回PayOrder和WxPayConfig对象
			if(!verifyWxPayParams(payContext)) {
				return WxPayNotifyResponse.fail((String) payContext.get("retMsg"));
			}
			PayOrder payOrder = (PayOrder) payContext.get("payOrder");
			// 这里做了签名校验(这里又做了一次xml转换对象,可以考虑优化)
			wxPayService.parseOrderNotifyResult(xmlResult);
			// 处理订单
			// 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
			byte payStatus = payOrder.getStatus();
			if (payStatus != PayConstant.PAY_STATUS_SUCCESS && payStatus != PayConstant.PAY_STATUS_COMPLETE) {
				int updatePayOrderRows = payOrderService.updateStatus4Success(payOrder.getPayOrderId());
				if (updatePayOrderRows != 1) {
					_log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
					return WxPayNotifyResponse.fail("处理订单失败");
				}
				_log.error("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
				payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
			}
			// 业务系统后端通知
			doNotify(payOrder);
			_log.info("====== 完成接收微信支付回调通知 ======");
			return WxPayNotifyResponse.success("处理成功");
		} catch (WxPayException e) {
			//出现业务错误
			_log.error(e, "微信回调结果异常,异常原因");
			_log.info("{}请求数据result_code=FAIL", logPrefix);
			_log.info("err_code:", e.getErrCode());
			_log.info("err_code_des:", e.getErrCodeDes());
			return WxPayNotifyResponse.fail(e.getMessage());
		} catch (Exception e) {
			_log.error(e, "微信回调结果异常,异常原因");
			return WxPayNotifyResponse.fail(e.getMessage());
		}
	}
	
	/**
	 * 验证微信支付通知参数
	 * @return
	 */
	public boolean verifyWxPayParams(Map<String, Object> payContext) {
		//微信返回的通知结果
		WxPayOrderNotifyResult params = (WxPayOrderNotifyResult)payContext.get("parameters");

		//校验结果是否成功
		if (!PayConstant.RETURN_VALUE_SUCCESS.equalsIgnoreCase(params.getResultCode())
				&& !PayConstant.RETURN_VALUE_SUCCESS.equalsIgnoreCase(params.getReturnCode())) {
			payContext.put("retMsg", "notify data failed");
			return false;
		}

		// 总金额
		Integer total_fee = params.getTotalFee();
		// 商户系统订单号
		String out_trade_no = params.getOutTradeNo();

		// 查询payOrder记录
		PayOrder payOrder = payOrderService.selectPayOrder(out_trade_no);
		if (payOrder==null) {
			payContext.put("retMsg", "Can't found payOrder");
			return false;
		}

		// 查询payChannel记录
		String channelId = payOrder.getChannelId();
		PayChannel payChannel = payChannelService.selectPayChannel(channelId);
		if(payChannel == null) {
			payContext.put("retMsg", "Can't found payChannel");
			return false;
		}

		String ccXzsxlx = payOrder.getXzsxlx();
		Customer customer = customerService.getById(ccXzsxlx);
		if(ObjectUtil.isNull(customer)){
			payContext.put("retMsg", "Can't found customer");
			return false;
		}

		// 核对金额
		long wxPayAmt = new BigDecimal(total_fee).longValue();
		long dbPayAmt = payOrder.getAmount().longValue();
		if (dbPayAmt != wxPayAmt) {
			payContext.put("retMsg", "total_fee is not the same");
			return false;
		}
		payContext.put("payOrder", payOrder);
		return true;
	}

}
