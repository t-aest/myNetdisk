package com.taest.mynetdisk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.taest.mynetdisk.*.mapper")
public class MynetdiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(MynetdiskApplication.class, args);
    }

}
