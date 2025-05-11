package com.nextfin.account.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountSearchOptions {
    @Builder.Default
    int pageSize = 5;
    
    @Builder.Default
    int skip = 0;
}
