package com.copote.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.copote.wechat.entity.PayChannel;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
public interface PayChannelService extends IService<PayChannel> {


    /**
     * 查询支付渠道
     * @param channelName
     * @return
     */
    PayChannel selectPayChannel(@Param("channelName")String channelName);

}
