package com.michaelvol.nextfin.config.security;

import com.michaelvol.nextfin.AppConstants;
import com.michaelvol.nextfin.auth.oauth2.service.OidcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final OidcService oidcUserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/api/v**/auth/login", "/api/v**/auth/register").permitAll();
                    request.requestMatchers("/api/v**/oauth2/**").permitAll();
                    request.requestMatchers("/api/public/**").permitAll();
                    request.requestMatchers("/error").permitAll();
                    request.requestMatchers("/api/**", "/actuator/**").fullyAuthenticated();
                });

        http.securityContext(context -> context.securityContextRepository(securityContextRepository()));

        http.sessionManagement(session -> {
            session.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession);
            session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        });

        http.logout(logout -> {
            logout.logoutUrl(AppConstants.API_BASE_URL + "/auth/logout");
            logout.logoutSuccessUrl(AppConstants.API_BASE_URL + "/auth/logout/success").permitAll();
            ClearSiteDataHeaderWriter clearSiteDataHeader = new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES);
            logout.addLogoutHandler(new HeaderWriterLogoutHandler(clearSiteDataHeader));
            logout.deleteCookies("JSESSIONID");
        });

        http.authenticationProvider(authenticationProvider);

        http.oauth2Login(oauth2 -> {
            String baseUrl = AppConstants.API_BASE_URL + "/oauth2";
            oauth2.authorizationEndpoint(auth -> auth.baseUri(baseUrl + "/authorization"));
            oauth2.defaultSuccessUrl(baseUrl + "/success", true).permitAll();
            oauth2.failureUrl(baseUrl + "/failure").permitAll();
            oauth2.userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService));
        });
        http.oauth2Client(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
}
