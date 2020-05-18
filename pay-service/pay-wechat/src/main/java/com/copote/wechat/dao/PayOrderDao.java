package com.copote.wechat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.copote.wechat.entity.PayOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * @author Administrator
 * @create 2020/5/14
 * @Description:
 * @since 1.0.0
 */
@Mapper
public interface PayOrderDao extends BaseMapper<PayOrder> {
}
