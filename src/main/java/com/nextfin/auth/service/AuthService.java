package com.nextfin.auth.service;

import com.nextfin.auth.dto.LoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface AuthService {

    void authenticate(LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response);

}
