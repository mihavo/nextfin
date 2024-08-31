package com.michaelvol.bankingapp.auth.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.auth.dto.LoginRequestDto;
import com.michaelvol.bankingapp.auth.dto.LoginResponseDto;
import com.michaelvol.bankingapp.auth.dto.RegisterRequestDto;
import com.michaelvol.bankingapp.auth.dto.RegisterResponseDto;
import com.michaelvol.bankingapp.auth.service.AuthService;
import com.michaelvol.bankingapp.users.entity.User;
import com.michaelvol.bankingapp.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(name = AppConstants.API_BASE_URL + "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    private final MessageSource messageSource;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) {
        authService.authenticate(loginRequestDto, request, response);
        return new ResponseEntity<>(new LoginResponseDto(messageSource.getMessage(
                "auth.login-success",
                null,
                LocaleContextHolder.getLocale()
        )), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        User user = userService.createUser(registerRequestDto.user());
        return new ResponseEntity<>(new RegisterResponseDto(messageSource.getMessage(
                "auth.register-success",
                null,
                LocaleContextHolder.getLocale()
        ),
                user), HttpStatus.CREATED);
    }
}
