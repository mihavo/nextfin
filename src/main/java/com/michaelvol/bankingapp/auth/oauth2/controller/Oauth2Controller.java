package com.michaelvol.bankingapp.auth.oauth2.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.michaelvol.bankingapp.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<JsonNode> oauth2Success() {
        JsonNode userInfo = webClient.get()
                                     .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                                     .retrieve().bodyToMono(JsonNode.class).block();
        System.out.println(userInfo);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}
