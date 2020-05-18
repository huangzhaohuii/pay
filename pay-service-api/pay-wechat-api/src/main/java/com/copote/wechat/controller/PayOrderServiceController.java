package com.copote.wechat.controller;

import com.copote.common.constant.PayConstant;
import com.copote.common.exception.R;
import com.copote.dal.model.PayOrder;
import com.copote.wechat.service.PayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PayOrderServiceController extends Notify4BasePay {

    @Autowired
    private PayOrderService payOrderService;

    /**
     * 创建订单
     * @param payOrder
     * @return
     */
    @RequestMapping(value = "/pay/create")
    public R createPayOrder(@RequestBody PayOrder payOrder) {
        log.info("接收创建支付订单请求,payOrder={}", payOrder);
        int result = 0;
        try {
            result = payOrderService.createPayOrder(payOrder);
        }catch (Exception e) {
            return R.error("系统错误");
        }
        return R.ok().put("result",result);
    }

    /**
     * 查询订单
     * @param map
     * @return
     */
    @RequestMapping(value = "/pay/query")
    public R queryPayOrder(@RequestBody Map<String,Object> map) {
        log.info("selectPayOrder << {}", map);
        String mchId = (String) map.get("mchId");
        String payOrderId = (String) map.get("payOrderId");
        String mchOrderNo = (String) map.get("mchOrderNo");
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
        boolean executeNotify = (boolean) map.get("executeNotify");
        // 如果选择回调且支付状态为支付成功,则回调业务系统
        if(executeNotify && payOrder.getStatus() == PayConstant.PAY_STATUS_SUCCESS) {
            this.doNotify(payOrder);
        }
        return R.ok().put("Object",payOrder);
    }

}
