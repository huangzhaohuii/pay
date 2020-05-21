package com.copote.wechat.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.copote.common.exception.R;
import com.copote.wechat.entity.MchInfo;
import com.copote.wechat.service.MchInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Administrator
 * @create 2020/5/11
 * @Description: 商户信息接口
 * @since 1.0.0
 */
@RestController
@Slf4j
public class MchInfoController {

    @Autowired
    private MchInfoService mchInfoService;

    /**
     * 查询商户
     * @param params
     * @return
     */
    @RequestMapping(value = "/mch_info/select")
    public R selectMchInfo(@RequestBody Map<String,Object> params) {
        String mchId = (String) params.get("mchId");
        MchInfo mchInfo = mchInfoService.getById(mchId);
        if(BeanUtil.isEmpty(mchInfo)) {
            return R.error("该商户信息不存在");
        }
        return R.ok().put("result",mchInfo);
    }



}
