package com.nextfin.auth.service.impl;

import com.nextfin.auth.dto.LoginRequestDto;
import com.nextfin.auth.service.AuthService;
import com.nextfin.users.dto.CreateUserDto;
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
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequestDto.username(),
                loginRequestDto.password());
        createSessionFromToken(token, request, response);
    }

    @Override
    public void createNewUserSession(CreateUserDto userDto, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(userDto.getUsername(),
                                                                                                        userDto.getPassword());
        createSessionFromToken(token, request, response);
    }

    private void createSessionFromToken(UsernamePasswordAuthenticationToken token, HttpServletRequest request,
                                        HttpServletResponse response) {
        Authentication authentication = authManager.authenticate(token);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);
    }
}
