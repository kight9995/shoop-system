package com.zxst.shoop;

import com.zxst.shoop.config.AliPayTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@MapperScan({"com.zxst.shoop.mapper", "com.zxst.shoop.service.impl"})
@SpringBootApplication
public class Shoop_Customer_Application {

    public static void main(String[] args) {
        SpringApplication.run(Shoop_Customer_Application.class, args);

    }
}
