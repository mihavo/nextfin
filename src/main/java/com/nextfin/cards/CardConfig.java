package com.nextfin.cards;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Configuration
public class CardConfig {

    @Bean
    public Faker FakerConfig() {
        return new Faker(new Locale(LocaleContextHolder.getLocale().toLanguageTag()));
    }
}
