package com.copote.wechat.controller;

import com.copote.common.constant.PayConstant;
import com.copote.common.exception.R;
import com.copote.wechat.entity.PayOrder;
import com.copote.wechat.service.NotifyService;
import com.copote.wechat.service.PayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @author Administrator
 * @create 2020/5/11
 * @Description: 订单支付接口
 * @since 1.0.0
 */
@RestController
@Slf4j
@RequestMapping("/pay")
@RefreshScope
public class PayOrderController extends Notify4BasePay {

    @Autowired
    private PayOrderService payOrderService;


    /**
     * 创建订单
     * @param payOrder
     * @return
     */
    @RequestMapping(value = "/create")
    public R createPayOrder(@RequestBody PayOrder payOrder) {
        log.info("接收创建支付订单请求,payOrder={}", payOrder);
        boolean result = payOrderService.save(payOrder);
        if(!result){
            return R.error("新增失败");
        }
        return R.ok();
    }

    /**
     * 查询订单(包含是否回调第三方业务)
     * @param params
     * @return
     */
    @RequestMapping(value = "/query")
    public R queryPayOrder(@RequestBody Map<String,Object> params) {
        log.info("selectPayOrder << {}", params);
        String mchId = (String) params.get("mchId");
        String payOrderId = (String) params.get("payOrderId");
        String mchOrderNo = (String) params.get("mchOrderNo");
        PayOrder payOrder;
        if(StringUtils.isNotBlank(payOrderId)) {
            payOrder = payOrderService.selectPayOrderByMchIdAndPayOrderId(mchId, payOrderId);
        }else {
            payOrder = payOrderService.selectPayOrderByMchIdAndMchOrderNo(mchId, mchOrderNo);
        }
        if(payOrder == null) {
            return R.error("订单不存在");
        }
        //是否回调
        boolean executeNotify = (boolean) params.get("executeNotify");
        // 如果选择回调且支付状态为支付成功,则回调业务系统
        if(executeNotify && payOrder.getStatus() == PayConstant.PAY_STATUS_SUCCESS) {
            this.doNotify(payOrder);
        }
        return R.ok().put("data",payOrder);
    }

}
