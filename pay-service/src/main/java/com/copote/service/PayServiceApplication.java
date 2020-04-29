package com.copote.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HUANG
 * @date 2020/4/29 18:36
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PayServiceApplication {
    public static void main(String[] args) {
            SpringApplication.run(PayServiceApplication.class, args);
        }
}
