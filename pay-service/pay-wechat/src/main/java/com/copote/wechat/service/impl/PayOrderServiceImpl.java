package com.copote.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.copote.common.constant.PayConstant;
import com.copote.wechat.dao.PayOrderDao;
import com.copote.wechat.entity.PayOrder;
import com.copote.wechat.service.PayOrderService;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @create 2020/5/13
 * @Description:
 * @since 1.0.0
 */
@Service("payOrderService")
public class PayOrderServiceImpl extends ServiceImpl<PayOrderDao, PayOrder> implements PayOrderService {

    @Override
    public int createPayOrder(PayOrder payOrder) {
        return baseMapper.insert(payOrder);
    }

    @Override
    public PayOrder selectPayOrder(String payOrderId) {
        return baseMapper.selectById(payOrderId);
    }

    @Override
    public PayOrder selectPayOrderByMchIdAndPayOrderId(String mchId, String payOrderId) {
        QueryWrapper<PayOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("mchId",mchId)
                .eq("payOrderId",payOrderId);
        return baseMapper.selectOne(wrapper);

    }

    @Override
    public PayOrder selectPayOrderByMchIdAndMchOrderNo(String mchId, String mchOrderNo) {
        QueryWrapper<PayOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("mchId",mchId)
                .eq("mchOrderNo",mchOrderNo);
        return baseMapper.selectList(wrapper).get(0);

    }

    @Override
    public int updateStatus4Ing(String payOrderId, String channelOrderNo) {
        UpdateWrapper<PayOrder> wrapper = new UpdateWrapper<>();
        wrapper.eq("payOrderId",payOrderId)
                .eq("channelOrderNo",channelOrderNo)
                .eq("status",PayConstant.PAY_STATUS_PAYING);
        return baseMapper.update(new PayOrder(),wrapper);

    }

    @Override
    public int updateStatus4Success(String payOrderId) {
        UpdateWrapper<PayOrder> wrapper = new UpdateWrapper<>();
        wrapper.eq("payOrderId",payOrderId)
                .eq("status",PayConstant.PAY_STATUS_SUCCESS)
                .eq("paySuccTime",System.currentTimeMillis());
        return baseMapper.update(new PayOrder(),wrapper);

    }

    @Override
    public int updateStatus4Complete(String payOrderId) {
        UpdateWrapper<PayOrder> wrapper = new UpdateWrapper<>();
        wrapper.eq("payOrderId",payOrderId)
                .eq("status",PayConstant.PAY_STATUS_COMPLETE);
        return baseMapper.update(new PayOrder(),wrapper);
    }

    @Override
    public int updateNotify(String payOrderId, byte count) {
        PayOrder newPayOrder = new PayOrder();
        // TODO 并发下次数问题待解决
        newPayOrder.setNotifyCount(count);
        newPayOrder.setLastNotifyTime(System.currentTimeMillis());
        newPayOrder.setPayOrderId(payOrderId);
        return baseMapper.updateById(newPayOrder);

    }

    @Override
    public int updateNotify(PayOrder payOrder) {
        return baseMapper.updateById(payOrder);
    }

    @Override
    public int update(PayOrder payOrder) {
        return 0;
    }

}
