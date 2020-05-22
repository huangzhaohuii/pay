package com.copote.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.copote.wechat.entity.PayOrder;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
public interface PayOrderService extends IService<PayOrder> {


    /**
     * 创建订单
     * @param payOrder
     * @return
     */
    int createPayOrder(PayOrder payOrder);

    /**
     * 查询订单
     * @param payOrderId
     * @return
     */
    PayOrder selectPayOrder(String payOrderId);

    /**
     * 查询
     * @param mchId
     * @param payOrderId
     * @return
     */
    PayOrder selectPayOrderByMchIdAndPayOrderId(String mchId, String payOrderId);

    /**
     * 通过商户号和订单号查询
     * @param mchId
     * @param mchOrderNo
     * @return
     */
    PayOrder selectPayOrderByMchIdAndMchOrderNo(String mchId, String mchOrderNo);

    /**
     * 更新状态为支付中
     * @param payOrderId
     * @param channelOrderNo
     * @return
     */
    int updateStatus4Ing(String payOrderId, String channelOrderNo);

    /**
     * 更新状态为支付成功
     * @param payOrderId
     * @return
     */
    int updateStatus4Success(String payOrderId);

    /**
     * 更新状态为支付完成
     * @param payOrderId
     * @return
     */
    int updateStatus4Complete(String payOrderId);

    /**
     * 更新通知
     * @param payOrderId
     * @param count
     * @return
     */
    int updateNotify(String payOrderId, byte count);

    /**
     * 更新通知
     * @param payOrder
     * @return
     */
    int updateNotify(PayOrder payOrder);


}
