package com.nextfin.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * App Configuration
 */
@Configuration
@EnableMethodSecurity
public class AppConfig {

    /**
     * Sets up a message source loader for loading messages used in controllers
     * @return the MessageSource
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages/controller-messages");
        messageSource.setDefaultLocale(LocaleContextHolder.getLocale());
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
