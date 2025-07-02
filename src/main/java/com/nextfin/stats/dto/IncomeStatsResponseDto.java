package com.nextfin.stats.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class IncomeStatsResponseDto {
    private BigDecimal totalIncome;
    private BigDecimal averageIncome;
    private LocalDate startDate;
    private LocalDate endDate;
}
