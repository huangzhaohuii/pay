package com.copote.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.copote.common.exception.R;
import com.copote.common.util.MyBase64;
import com.copote.common.util.MyLog;
import com.copote.wechat.entity.PayChannel;
import com.copote.wechat.service.PayChannelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 支付渠道接口
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
public class PayChannelServiceController {

    private final MyLog _log = MyLog.getLog(PayChannelServiceController.class);

    @Autowired
    private PayChannelService payChannelService;

    @RequestMapping(value = "/pay_channel/select")
    public R selectPayChannel(@RequestParam String jsonParam) {
        // TODO 参数校验
        _log.info("selectPayChannel << {}", jsonParam);
        if(StringUtils.isBlank(jsonParam)) {
            R.error("参数错误");
        }
        JSONObject paramObj = JSON.parseObject(new String(MyBase64.decode(jsonParam)));
        String channelId = paramObj.getString("channelId");
        String mchId = paramObj.getString("mchId");
        PayChannel payChannel = payChannelService.selectPayChannel(channelId, mchId);
        if(payChannel == null) {
            R.error("数据对象不存在");
        }
        _log.info("selectPayChannel >> {}", payChannel);
        return R.ok().put("result",payChannel);
    }


}
