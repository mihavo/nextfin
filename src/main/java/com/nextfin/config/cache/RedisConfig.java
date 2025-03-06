package com.nextfin.config.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${nextfin.cache.host}")
    private String host;

    @Value("${nextfin.cache.port:6379}")
    private Integer port;

    @Value("${nextfin.cache.password}")
    private String password;

    @Value("${nextfin.cache.database:0}")
    private Integer database;

    @Value("${nextfin.cache.allowed:true}")
    private Boolean isCachingAllowed;

    private static final AtomicBoolean isCachingEnabled = new AtomicBoolean(false);

    @Bean
    public RedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        JedisConnectionFactory connFactory = new JedisConnectionFactory(redisConfig);
        if (!isCachingAllowed || host == null) {
            log.warn(
                    "Caching is explicitly disabled from environment or misconfigured;  attempted connections will " +
                            "fail.");
            return constructMinimalFactory();
        }
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setDatabase(database);
        redisConfig.setPassword(RedisPassword.of(password));
        return evaluateConnection(connFactory);
    }

    private static @NotNull JedisConnectionFactory constructMinimalFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName("localhost");
        redisConfig.setPort(6379);
        redisConfig.setDatabase(0);
        redisConfig.setPassword(RedisPassword.none());
        return new JedisConnectionFactory(redisConfig) {
            @Override
            public @NotNull RedisConnection getConnection() {
                throw new UnsupportedOperationException("Cannot connect to cache");
            }
        };
    }

    public RedisConnectionFactory evaluateConnection(JedisConnectionFactory connFactory) {
        connFactory.start();
        try (RedisConnection connection = connFactory.getConnection()) {
            String ping = connection.ping();
            if (ping != null && ping.equals("PONG")) {
                String connectionId = "nextfin-cache-" + UUID.randomUUID();
                connection.serverCommands().setClientName(connectionId.getBytes(StandardCharsets.UTF_8));
                log.info("Connection with Cache client established: {}", connection.serverCommands().getClientName());
                isCachingEnabled.set(true);
                return connFactory;
            }
            throw new RuntimeException("Connection could not be established: unexpected response");
        } catch (Exception e) {
            log.error("Connection with Cache Client failed: {}", e.getMessage());
        }
        return connFactory;
    }

}
