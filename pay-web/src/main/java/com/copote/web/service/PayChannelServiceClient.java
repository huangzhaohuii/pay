package com.copote.web.service;

import com.copote.common.util.MyBase64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Description:
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@Service
public class PayChannelServiceClient {

    @Autowired
    RestTemplate restTemplate;

//    @HystrixCommand(fallbackMethod = "selectPayChannelFallback")
    public String selectPayChannel(String jsonParam) {
        return restTemplate.getForEntity("http://XXPAY-SERVICE/pay_channel/select?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String selectPayChannelFallback(String jsonParam) {
        return "error";
    }

}