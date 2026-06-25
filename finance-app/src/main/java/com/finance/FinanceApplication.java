package com.finance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 新一代智能财务管理系统 - 启动类
 *
 * @author finance
 */
@SpringBootApplication
@MapperScan("com.finance.module.**.mapper")
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
public class FinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("新一代智能财务管理系统 启动成功");
        System.out.println("API 地址: http://localhost:8080/api");
        System.out.println("接口文档: http://localhost:8080/api/doc.html");
        System.out.println("========================================\n");
    }
}
