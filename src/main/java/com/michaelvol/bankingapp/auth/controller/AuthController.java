package com.michaelvol.bankingapp.auth.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.auth.dto.LoginRequestDto;
import com.michaelvol.bankingapp.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(name = AppConstants.API_BASE_URL + "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginRequestDto request) {

    }
}
