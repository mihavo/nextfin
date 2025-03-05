package com.nextfin.config.cache;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RedisConfig {

    private JedisConnectionFactory connFactory;

    @Value("${nextfin.cache.host:#{null}}")
    private String host;

    @Value("${nextfin.cache.port:#{null}}")
    private Integer port;

    @Value("${nextfin.cache.password:#{null}}")
    private String password;

    @Value("${nextfin.cache.database:#{null}}")
    private Integer database;

    @Value("${nextfin.cache.allowed:true}")
    private Boolean isCachingAllowed;

    private static final AtomicBoolean isCachingEnabled = new AtomicBoolean(false);

    @Bean
    public RedisConnectionFactory connectionFactory() {
        if (!isCachingAllowed || ObjectUtils.anyNull(host, port, password, database)) {
            return null;
        }
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setDatabase(database);
        redisConfig.setPassword(password);
        return new JedisConnectionFactory(redisConfig);
    }

    @PostConstruct
    public void evaluateConnection() {
        try (RedisConnection connection = connFactory.getConnection()) {
            String ping = connection.ping();
            if (ping != null && ping.equals("PONG")) {
                log.info("Connection with Cache client established: {}", connection.getClientName());
                isCachingEnabled.set(true);
                return;
            }
            throw new RuntimeException("Connection could not be established: unexpected response");
        } catch (Exception e) {
            log.error("Connection with Cache Client failed: {}", e.getMessage());
        }
    }

}
