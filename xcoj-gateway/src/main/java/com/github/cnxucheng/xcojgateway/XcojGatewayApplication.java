package com.github.cnxucheng.xcojgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan("com.github.cnxucheng")
@EnableFeignClients(basePackages = {"com.github.cnxucheng.xcojfeignclient.service"})
public class XcojGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(XcojGatewayApplication.class, args);
    }

}
