package com.lbz.gmall;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan(basePackages = "com.lbz.gmall.payment.mapper")
@EnableDubbo
@SpringBootApplication
public class GmallPaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPaymentServiceApplication.class, args);
    }

}
