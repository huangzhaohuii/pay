package com.copote.wechat.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.copote.common.constant.PayConstant;
import com.copote.common.exception.R;
import com.copote.common.util.JsonUtil;
import com.copote.common.util.MySeq;
import com.copote.common.util.RUtil;
import com.copote.common.util.XXPayUtil;
import com.copote.wechat.entity.PayOrder;
import com.copote.wechat.entity.WeChatEntity;
import com.copote.wechat.service.MchInfoService;
import com.copote.wechat.service.PayChannelService;
import com.copote.wechat.service.PayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Administrator
 * @create 2020/5/14
 * @Description: 进一步检验创建微信订单入参
 * @since 1.0.0
 */
@Component
@Slf4j
public class ValidateWeChatParamsUtil {

    @Autowired
    private PayChannelService payChannelService;

    @Autowired
    private MchInfoService mchInfoService;

    /**
     * 验证签名信息
     * @param payOrder
     * @return
     */
    public R validateParams(PayOrder payOrder) {
        // 商户ID
        String mchId = payOrder.getMchId();
        // 渠道ID
        String channelId = payOrder.getChannelId();
        // 签名
        String sign = payOrder.getSign();

        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            return R.error("签名信息为空");
        }

        // 查询商户信息
        R r = selectMchInfo(mchId);
        if(RUtil.checkErro(r)){
            return r;
        }
        //密钥
        String reqKey = (String) ((JSONObject)r.get("data")).get("reqKey");

        //查询渠道信息
        r = selectPayChannel(channelId,mchId);
        if(RUtil.checkErro(r)){
            return r;
        }

        // 验证签名数据
        boolean verifyFlag = XXPayUtil.verifyPaySign(BeanUtil.beanToMap(payOrder), reqKey);
        if(!verifyFlag) {
            return R.error("签名错误");
        }
        return R.ok();
    }


    /**
     * 校验查询参数
     * @param map
     * @return
     */
    public R validateQueryParams(Map<String,String> map){
        // 签名
        String sign = map.get("sign");
        // 商户ID
        String mchId = map.get("mchId");
        // 商户订单号
        String mchOrderNo = map.get("mchOrderNo");
        // 支付订单号
        String payOrderId = map.get("payOrderId");
        // 查询商户信息
        // 验证请求参数有效性（必选项）
        if(StringUtils.isBlank(mchId)) {
            return R.error("参数[mchId]为空");
        }
        if(StringUtils.isBlank(mchOrderNo) && StringUtils.isBlank(payOrderId)) {
            return R.error("参数[mchOrderNo]或者[payOrderId]为空");
        }
        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            return R.error("参数[sign]为空");
        }
        R r = selectMchInfo(mchId);
        return r;
    }


    /**
     * 查询商户信息
     * @param mchId
     * @return
     */
    private R selectMchInfo(String mchId){
        // 查询商户信息
        R r = mchInfoService.selectMchInfo(JsonUtil.getJsonParam("mchId", mchId));
        if(!"00".equals(r.get("code"))) {
            return R.error("商户信息错误，未找到[mchId]");
        }
        String state = (String) ((JSONObject)r.get("data")).get("state");
        if(StrUtil.isEmpty(state) && StrUtil.equals("1",state)){
            return R.error("商户信息错误");
        }
        String reqKey = (String) ((JSONObject)r.get("data")).get("reqKey");
        if (StringUtils.isBlank(reqKey)) {
            return R.error("商户信息错误，未找到[reqKey]");
        }
        return r;
    }


    /**
     * 查询商户对应的支付渠道
     * @param channelId
     * @param mchId
     * @return
     */
    private R selectPayChannel(String channelId,String mchId){
        R r = payChannelService.selectPayChannel(JsonUtil.getJsonParam(new String[]{"channelId", "mchId"}, new String[]{channelId, mchId}));
        if(!"00".equals(r.get("code"))) {
            return R.error("渠道信息错误，未找到[channelId,mchId]");
        }
        String state = (String) ((JSONObject)r.get("data")).get("state");
        if(StrUtil.isEmpty(state) && StrUtil.equals("1",state)){
            return R.error("渠道信息错误");
        }
        return r;
    }
}
