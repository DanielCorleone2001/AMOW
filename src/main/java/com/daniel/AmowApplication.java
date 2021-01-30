package com.daniel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.daniel.mapper")
public class AmowApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmowApplication.class, args);
    }

}
