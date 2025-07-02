package com.nextfin.stats.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ExpensesStatsResponseDto {
    private BigDecimal totalExpenses;
    private BigDecimal averageExpense;
    private LocalDate startDate;
    private LocalDate endDate;
}
