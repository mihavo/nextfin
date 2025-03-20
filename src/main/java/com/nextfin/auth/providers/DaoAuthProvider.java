package com.nextfin.auth.providers;

import com.nextfin.users.service.impl.NextfinUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DaoAuthProvider {

    private final NextfinUserDetailsService nextfinUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(nextfinUserDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder);
        return daoProvider;
    }
}
