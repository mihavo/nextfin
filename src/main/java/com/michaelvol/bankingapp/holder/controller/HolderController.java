package com.michaelvol.bankingapp.holder.controller;


import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.holder.dto.CreateHolderRequestDto;
import com.michaelvol.bankingapp.holder.dto.CreateHolderResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/holders")
public class HolderController {

    public ResponseEntity<CreateHolderResponseDto> createHolder(@RequestBody CreateHolderRequestDto request) {
        return new ResponseEntity<>(CreateHolderResponseDto.builder().build(),
                                    HttpStatus.CREATED);
    }
}
