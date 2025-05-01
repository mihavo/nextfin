package com.nextfin.config.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
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

    @Value("${nextfin.cache.useSsl:true}")
    private Boolean useSsl;

    private static final AtomicBoolean isCachingEnabled = new AtomicBoolean(false);

    private static RedisConnectionFactory connFactory;

    @Bean
    public RedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        if (!isCachingAllowed || host == null) {
            log.warn("Caching is explicitly disabled from environment or misconfigured; attempted connections will " + "fail.");
            return constructMinimalFactory();
        }
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setDatabase(database);
        redisConfig.setPassword(password == null ? RedisPassword.none() : RedisPassword.of(password));
        connFactory = new JedisConnectionFactory(redisConfig, buildClientConfig());
        ((JedisConnectionFactory) connFactory).afterPropertiesSet();
        evaluateConnection(connFactory);
        return connFactory;
    }

    private @NotNull JedisClientConfiguration buildClientConfig() {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        builder.connectTimeout(Duration.ofSeconds(120));
        builder.usePooling().poolConfig(buildPoolConfig());
        if (useSsl) builder.useSsl();
        return builder.build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setBeanClassLoader(Thread.currentThread().getContextClassLoader());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setBeanClassLoader(Thread.currentThread().getContextClassLoader());
        return template;
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

    public RedisConnectionFactory evaluateConnection(RedisConnectionFactory connFactory) {
        ((JedisConnectionFactory) connFactory).start();
        try (RedisConnection conn = connFactory.getConnection()) {
            if (pingCache(conn)) {
                String connId = "nextfin-cache-" + UUID.randomUUID();
                conn.serverCommands().setClientName(connId.getBytes(StandardCharsets.UTF_8));
                log.debug("Connection with Cache client established: {}", conn.serverCommands().getClientName());
                isCachingEnabled.set(true);
            } else log.error("Connection could not be established: unexpected response");
        } catch (RedisConnectionFailureException e) { // only catches connection failures
            log.error(e.getMessage());
            isCachingEnabled.set(false);
        } catch (Exception e) {
            log.error("Connection with Cache Client failed: {}", e.getMessage());
        }
        return connFactory;
    }

    private boolean pingCache(RedisConnection connection) {
        String ping = connection.ping();
        return ping != null && ping.equals("PONG");
    }

    @Scheduled(fixedRate = 30000)  // Re-check connection every 30 seconds
    public void reEvaluateConnection() {
        try {
            // no connection established in the first place, re-get the connection and try again 
            if (!pingCache(connFactory.getConnection())) {
                isCachingEnabled.set(false); // failed to get connection
                return;
            }
            // connection successful, check for previous unsuccessful attempts and re-enable the flag
            if (!isCachingEnabled()) {
                isCachingEnabled.set(true);
                log.debug("Cache connection re-established");
            }
        } catch (RedisConnectionFailureException e) { // only catches connection failures
            log.error(e.getMessage());
            isCachingEnabled.set(false);
        } catch (Exception e) {  // only catches unhandled exceptions
            log.error("Failed to recheck Redis connection: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static boolean isCachingEnabled() {
        return isCachingEnabled.get();
    }

    private static @NotNull JedisPoolConfig buildPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxWait(Duration.ofSeconds(120));
        return poolConfig;
    }
}
