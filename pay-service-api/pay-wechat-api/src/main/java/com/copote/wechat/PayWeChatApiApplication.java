package com.copote.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Administrator
 * @create 2020/5/14
 * @Description:
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PayWeChatApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayWeChatApiApplication.class, args);
    }
}
