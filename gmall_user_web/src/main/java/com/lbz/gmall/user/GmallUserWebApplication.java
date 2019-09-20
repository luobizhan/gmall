package com.lbz.gmall.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo(scanBasePackages = "com.lbz.gmall.user.controller")
@SpringBootApplication
public class GmallUserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallUserWebApplication.class, args);
    }

}
