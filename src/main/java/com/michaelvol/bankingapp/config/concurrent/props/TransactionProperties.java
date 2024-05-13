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

    public TransactionProperties(int corePoolSize, int maxPoolSize, int queueCapacity) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.queueCapacity = queueCapacity;
    }
}
