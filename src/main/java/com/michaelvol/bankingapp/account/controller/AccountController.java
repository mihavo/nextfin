package com.michaelvol.bankingapp.account.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.dto.CreateAccountResponseDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("")
    public ResponseEntity<CreateAccountResponseDto> createAccount(@RequestBody CreateAccountRequestDto dto) {
        Account account = accountService.createAccount(dto);
        CreateAccountResponseDto responseDto = new CreateAccountResponseDto(account.getId(),
                                                                            "Your account has been created successfully");
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
