package com.michaelvol.bankingapp.account.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.account.dto.CreateAccountRequestDto;
import com.michaelvol.bankingapp.account.dto.CreateAccountResponseDto;
import com.michaelvol.bankingapp.account.dto.GetAccountResponseDto;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.service.AccountService;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{id}")
    public ResponseEntity<GetAccountResponseDto> getAccountData(@PathVariable(name = "id") @NotNull @Positive Long accountId) {
        Account account = accountService.getAccount(accountId);
        GetAccountResponseDto responseDto = new GetAccountResponseDto(account, "Fetched account successfully");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
