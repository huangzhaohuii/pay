package com.copote.wechat;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author HUANG
 * @date 2020/4/29 18:36
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class PayWeChatApplication {
    public static void main(String[] args) {
            SpringApplication.run(PayWeChatApplication.class, args);
        }
}
