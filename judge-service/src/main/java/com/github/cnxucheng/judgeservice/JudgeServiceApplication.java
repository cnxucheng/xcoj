package com.github.cnxucheng.judgeservice;

import com.github.cnxucheng.judgeservice.rabbitmq.InitMQ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession
@ComponentScan("com.github.cnxucheng")
@EnableDiscoveryClient
@EnableFeignClients("com.github.cnxucheng.xcojfeignclient.service")
public class JudgeServiceApplication {

    public static void main(String[] args) {
        InitMQ.run();
        SpringApplication.run(JudgeServiceApplication.class, args);
    }

}
