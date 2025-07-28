package com.nextfin.onboarding.filters;

import com.nextfin.auth.enums.OnboardingStep;
import com.nextfin.onboarding.service.OnboardingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OnboardingFilter extends OncePerRequestFilter {
    private final OnboardingService onboardingService;

    @Value("${nextfin.frontend.base-url}")
    private String defaultBaseUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean onboardingComplete = onboardingService.getCurrentUserOnboardingStep().equals(OnboardingStep.COMPLETED);
        if (!onboardingComplete) {
            response.sendRedirect(defaultBaseUrl + "/onboarding");
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals(
                "anonymousUser");
    }
}
