package com.sankin.diamond;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sankin.diamond.mapper")
public class DiamondApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiamondApplication.class, args);
    }

}
