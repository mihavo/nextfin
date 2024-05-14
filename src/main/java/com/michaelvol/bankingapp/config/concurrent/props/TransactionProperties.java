package com.michaelvol.bankingapp.config.concurrent.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "transaction.executor")
@Getter
@Setter
public class TransactionProperties {
    private final int corePoolSize;

    private final int maxPoolSize;

    private final int queueCapacity;

    private final int keepAliveSeconds;

    public TransactionProperties(int corePoolSize, int maxPoolSize, int queueCapacity, int keepAliveSeconds) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.queueCapacity = queueCapacity;
        this.keepAliveSeconds = keepAliveSeconds;
    }
}
