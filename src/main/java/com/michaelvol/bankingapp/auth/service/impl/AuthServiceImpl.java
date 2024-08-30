package com.michaelvol.bankingapp.auth.service.impl;

import com.michaelvol.bankingapp.auth.dto.LoginRequestDto;
import com.michaelvol.bankingapp.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;

    private final SecurityContextRepository securityContextRepository;

    @Override
    public void authenticate(LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken requestDetailsToken = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequestDto.username(),
                loginRequestDto.password());

        Authentication authentication = authManager.authenticate(requestDetailsToken);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);
    }

}
