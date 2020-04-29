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
public class MchInfoServiceClient {

    @Autowired
    RestTemplate restTemplate;

//    @HystrixCommand(fallbackMethod = "selectMchInfoFallback")
    public String selectMchInfo(String jsonParam) {
        return restTemplate.getForEntity("http://XXPAY-SERVICE/mch_info/select?jsonParam=" + MyBase64.encode(jsonParam.getBytes()), String.class).getBody();
    }

    public String selectMchInfoFallback(String jsonParam) {
        return "error";
    }

}