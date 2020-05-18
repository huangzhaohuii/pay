package com.copote.wechat.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.copote.wechat.entity.PayChannel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Administrator
 * @create 2020/5/14
 * @Description:
 * @since 1.0.0
 */
@Mapper
public interface PayChannelDao extends BaseMapper<PayChannel> {
}
