package com.copote.wechat.controller;

import cn.hutool.core.util.ObjectUtil;
import com.copote.common.exception.R;
import com.copote.common.util.MyLog;
import com.copote.wechat.entity.PayChannel;
import com.copote.wechat.service.PayChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description: 支付渠道接口
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
@Slf4j
public class PayChannelController {

    @Autowired
    private PayChannelService payChannelService;

    @RequestMapping(value = "/pay_channel/select")
    public R selectPayChannel(@RequestBody String channelName) {
        PayChannel payChannel = payChannelService.selectPayChannel(channelName);
        if(ObjectUtil.isNull(payChannel)) {
            R.error("渠道不存在");
        }
        return R.ok().put("data",payChannel);
    }


}
