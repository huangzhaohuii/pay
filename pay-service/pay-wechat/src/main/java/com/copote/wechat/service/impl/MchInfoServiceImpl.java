package com.copote.wechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.copote.wechat.dao.MchInfoDao;
import com.copote.wechat.entity.MchInfo;
import com.copote.wechat.service.MchInfoService;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @create 2020/5/14
 * @Description:
 * @since 1.0.0
 */
@Service("mchInfoService")
public class MchInfoServiceImpl extends ServiceImpl<MchInfoDao, MchInfo> implements MchInfoService {
}
