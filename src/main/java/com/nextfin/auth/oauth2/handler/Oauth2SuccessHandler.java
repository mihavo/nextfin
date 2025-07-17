package com.nextfin.auth.oauth2.handler;

import com.nextfin.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Value("${nextfin.frontend.base-url}")
    private String defaultBaseUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OidcUser principal = (OidcUser) authentication.getPrincipal();
        boolean onboardingComplete = userService.isOnboardingComplete(principal.getEmail());
        String redirectUri = defaultBaseUrl + (onboardingComplete ? "/onboarding" : "/");
        response.sendRedirect(redirectUri);

    }
} 
