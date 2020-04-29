package com.copote.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HUANG
 * @date 2020/4/29 18:20
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PayServerApplication {
    public static void main(String[] args) {
            SpringApplication.run(PayServerApplication.class, args);
    }
}
