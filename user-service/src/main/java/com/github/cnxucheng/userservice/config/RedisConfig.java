package com.github.cnxucheng.userservice.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisConfig {

    private String host;

    private int port;

    private Integer database;

    @Bean
    public RedissonClient redisConnection() {
        System.out.println(">>> RedisConfig host=" + host + ", port=" + port + ", db=" + database);
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setDatabase(database);
        return Redisson.create(config);
    }
}
