package com.sankin.diamond;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.sankin.diamond.mapper")
@ServletComponentScan
public class DiamondApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiamondApplication.class, args);
    }

}
