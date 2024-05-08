package com.michaelvol.bankingapp.transaction.dto;

import com.michaelvol.bankingapp.transaction.enums.TransactionSortingOptions;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.domain.Sort;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class GetTransactionOptions {

    @Builder.Default
    TransactionDirection direction = TransactionDirection.ALL;

    @Positive
    @Max(100)
    @Builder.Default
    Integer pageSize = 20;

    @PositiveOrZero
    @Builder.Default
    Integer skip = 0;

    @Builder.Default
    TransactionSortingOptions sortBy = TransactionSortingOptions.CREATED_AT;

    @Builder.Default
    Sort.Direction sortDirection = Sort.Direction.DESC;

}
