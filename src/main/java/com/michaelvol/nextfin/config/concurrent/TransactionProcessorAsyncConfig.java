package com.michaelvol.nextfin.config.concurrent;

import com.michaelvol.nextfin.config.concurrent.props.TransactionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * Defines the configuration necessary for implementing asynchronous financial
 * transactions in a concurrent way using {@link ThreadPoolTaskExecutor}
 */
@Configuration
@EnableAsync
@RequiredArgsConstructor
@EnableConfigurationProperties(TransactionProperties.class)
public class TransactionProcessorAsyncConfig {

    private final TransactionProperties properties;

    @Bean
    public ThreadPoolTaskExecutor transactionTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        executor.setCorePoolSize(properties.getCorePoolSize());
        executor.setQueueCapacity(properties.getQueueCapacity());
        executor.initialize();
        return executor;
    }
}
