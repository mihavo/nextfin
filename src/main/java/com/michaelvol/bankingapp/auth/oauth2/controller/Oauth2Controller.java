package com.michaelvol.bankingapp.auth.oauth2.controller;

import com.michaelvol.bankingapp.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@RequestMapping(AppConstants.API_BASE_URL + "/oauth2")
@RequiredArgsConstructor
public class Oauth2Controller {

    private final WebClient webClient;

    @GetMapping("/success")
    public void oauth2Success() {
        //get google user info
        String userInfo = webClient.get()
                                   .uri("https://www.googleapis.com/auth/userinfo.profile")
                                   .retrieve().bodyToMono(String.class).block();
        System.out.println(userInfo);
    }
}
