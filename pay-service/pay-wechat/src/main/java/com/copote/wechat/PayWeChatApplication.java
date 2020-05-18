package com.copote.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author HUANG
 * @date 2020/4/29 18:36
 */
@SpringBootApplication
@EnableFeignClients
public class PayWeChatApplication {
    public static void main(String[] args) {
            SpringApplication.run(PayWeChatApplication.class, args);
        }
}
