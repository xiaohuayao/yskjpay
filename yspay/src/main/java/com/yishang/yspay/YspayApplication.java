package com.yishang.yspay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.yishang.yspay.mapper")
public class YspayApplication {

    public static void main(String[] args) {
        SpringApplication.run(YspayApplication.class, args);
    }

}
