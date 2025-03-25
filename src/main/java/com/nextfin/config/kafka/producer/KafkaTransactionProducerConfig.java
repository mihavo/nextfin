package com.nextfin.config.kafka.producer;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.nextfin.transaction.entity.Transaction;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTransactionProducerConfig {

    @Bean
    public ProducerFactory<String, Transaction> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Transaction> transactionKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
