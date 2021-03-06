package com.copote.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.copote.wechat.dao.PayChannelDao;
import com.copote.wechat.entity.PayChannel;
import com.copote.wechat.service.PayChannelService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @create 2020/5/14
 * @Description:
 * @since 1.0.0
 */
@Service("payChannelService")
public class PayChannelServiceImpl extends ServiceImpl<PayChannelDao, PayChannel> implements PayChannelService {


    @Override
    public PayChannel selectPayChannel(@Param("channelName") String channelName) {
        QueryWrapper<PayChannel> wrapper = new QueryWrapper<>();
        wrapper.eq("channel_name",channelName)
                .eq("state",1);
        return baseMapper.selectList(wrapper).get(0);
    }
}
