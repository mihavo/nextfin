package com.nextfin.account.dto;

import lombok.*;

import java.util.Currency;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountSearchResultDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Currency currency;
}
