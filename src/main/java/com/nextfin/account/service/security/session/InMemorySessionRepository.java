package com.nextfin.account.service.security.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class InMemorySessionRepository {

    @Bean
    public MapSessionRepository inMemSessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }
}
