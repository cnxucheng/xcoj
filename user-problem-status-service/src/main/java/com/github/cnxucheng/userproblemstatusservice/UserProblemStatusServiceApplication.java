package com.github.cnxucheng.userproblemstatusservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan("com.github.cnxucheng.userproblemstatusservice.mapper")
@EnableRedisHttpSession
@ComponentScan("com.github.cnxucheng")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.github.cnxucheng.xcojfeignclient.service"})
class UserProblemStatusServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserProblemStatusServiceApplication.class, args);
    }
}
