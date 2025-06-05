package com.nextfin.auth.controller;

import com.nextfin.AppConstants;
import com.nextfin.auth.dto.LoginRequestDto;
import com.nextfin.auth.dto.LoginResponseDto;
import com.nextfin.auth.dto.RegisterResponseDto;
import com.nextfin.auth.service.AuthService;
import com.nextfin.users.dto.CreateUserDto;
import com.nextfin.users.entity.User;
import com.nextfin.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    private final MessageSource messageSource;

    @PostMapping("login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                                  HttpServletRequest request, HttpServletResponse response) {
        authService.authenticate(loginRequestDto, request, response);
        userService.cacheAccounts();
        return new ResponseEntity<>(
                new LoginResponseDto(messageSource.getMessage("auth.login-success", null, LocaleContextHolder.getLocale())),
                HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody CreateUserDto userDto, HttpServletRequest request,
                                                        HttpServletResponse response) {
        User user = userService.createUser(userDto);
        authService.createNewUserSession(userDto, request, response);
        return new ResponseEntity<>(
                new RegisterResponseDto(messageSource.getMessage("auth.register-success", null, LocaleContextHolder.getLocale()),
                                        user), HttpStatus.CREATED);
    }

    @GetMapping("logout/success")
    public ResponseEntity<String> logoutSuccess() {
        return new ResponseEntity<>(messageSource.getMessage(
                "auth.logout-success",
                null,
                LocaleContextHolder.getLocale()
        ), HttpStatus.OK);
    }

    @GetMapping("me")
    public ResponseEntity<User> getMe() {
        return new ResponseEntity<>(userService.getCurrentUser(), HttpStatus.OK);
    }
}
