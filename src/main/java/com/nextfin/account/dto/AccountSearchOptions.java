package com.nextfin.account.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
public class AccountSearchOptions {
    @Builder.Default
    int pageSize = 0;
    
    @Builder.Default
    int skip = 0;
}
