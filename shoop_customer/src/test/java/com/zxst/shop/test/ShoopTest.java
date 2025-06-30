package com.zxst.shop.test;

import com.zxst.shoop.Shoop_Customer_Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootTest(classes = {Shoop_Customer_Application.class})
public class ShoopTest {

    @Resource
    private DataSource dataSource;

  /*  @Test
    public void test() {
        System.out.println(dataSource);
    }*/
}
