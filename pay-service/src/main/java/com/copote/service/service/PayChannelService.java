package com.copote.service.service;

import com.copote.dal.dao.PayChannelMapper;
import com.copote.dal.model.PayChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class PayChannelService {

    @Autowired
    private PayChannelMapper payChannelMapper;

    public PayChannel selectPayChannel(String channelId, String mchId) {
//        PayChannelExample example = new PayChannelExample();
//        PayChannelExample.Criteria criteria = example.createCriteria();
//        criteria.andChannelIdEqualTo(channelId);
//        criteria.andMchIdEqualTo(mchId);
//        List<PayChannel> payChannelList = payChannelMapper.selectByExample(example);
//        if(CollectionUtils.isEmpty(payChannelList)) return null;
//        return payChannelList.get(0);
        return null;
    }

}
