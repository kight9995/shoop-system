package com.zxst.shoop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.zxst.shoop.mapper")
@SpringBootApplication
public class Shoop_Managent_Application {
    public static void main(String[] args) {
        SpringApplication.run(Shoop_Managent_Application.class, args);
    }
}
