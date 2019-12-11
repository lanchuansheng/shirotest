package com.lcs;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.lcs.mapper.**"})
public class ShirotestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShirotestApplication.class, args);
    }

}
