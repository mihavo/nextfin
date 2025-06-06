package com.nextfin.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "nextfin.embedded-executor", havingValue = "false")
public class KafkaAdminConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${com.nextfin.transactions.partitions:3}")
    private Integer transactionPartitions;

    @Value("${com.nextfin.transactions.replicas:3}")
    private Integer transactionReplicas;


    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic transactionsTopic() {
        return TopicBuilder.name("transactions").partitions(transactionPartitions).replicas(transactionReplicas).build();
    }
}
