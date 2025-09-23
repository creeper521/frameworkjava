package com.bitejiuyeke.bitemstemplateservice;

import com.bitejiuyeke.bitecommonsecurity.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 启动类
 */
@Slf4j
@Import(GlobalExceptionHandler.class)
@MapperScan("com.bitejiuyeke.**.mapper")
@SpringBootApplication
public class BiteMstemplateServiceApplication {

    /**
     * 主函数
     * @param args 入参
     */
    public static void main(String[] args) {
        SpringApplication.run(BiteMstemplateServiceApplication.class, args);
    }
}