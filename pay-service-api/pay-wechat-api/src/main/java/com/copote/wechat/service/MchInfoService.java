package com.copote.wechat.service;

import com.copote.dal.dao.MchInfoMapper;
import com.copote.dal.model.MchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Component
public class MchInfoService {

    @Autowired
    private MchInfoMapper mchInfoMapper;

    public MchInfo selectMchInfo(String mchId) {
        return mchInfoMapper.selectById(mchId);
    }

}
