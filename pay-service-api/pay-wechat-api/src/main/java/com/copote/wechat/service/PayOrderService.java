package com.copote.wechat.service;

import com.baomidou.mybatisplus.service.IService;
import com.copote.dal.model.PayOrder;

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

    PayOrder selectPayOrderByMchIdAndPayOrderId(String mchId, String payOrderId);

    PayOrder selectPayOrderByMchIdAndMchOrderNo(String mchId, String mchOrderNo);

    int updateStatus4Ing(String payOrderId, String channelOrderNo);

    int updateStatus4Success(String payOrderId);

    int updateStatus4Complete(String payOrderId);

    int updateNotify(String payOrderId, byte count);

    int updateNotify(PayOrder payOrder);

    int update(PayOrder payOrder);

}
