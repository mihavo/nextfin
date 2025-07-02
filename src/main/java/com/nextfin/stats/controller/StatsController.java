package com.nextfin.stats.controller;

import com.nextfin.AppConstants;
import com.nextfin.stats.dto.ExpensesStatsResponseDto;
import com.nextfin.stats.dto.IncomeStatsResponseDto;
import com.nextfin.stats.enums.StatsRange;
import com.nextfin.stats.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/stats")
@Tag(name = "Accounts API", description = "Methods for statistics")
@AllArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("income")
    @Operation(summary = "Income statistics", description = "Method for getting income statistics on a specified time range")
    public ResponseEntity<IncomeStatsResponseDto> incomeStats(@RequestParam(value = "range", defaultValue = "MONTHLY") StatsRange range) {
        IncomeStatsResponseDto stats = statsService.getIncomeStats(range);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("expenses")
    @Operation(summary = "Expenses statistics", description = "Method for getting expenses statistics on a specified time range")
    public ResponseEntity<ExpensesStatsResponseDto> expensesStats(@RequestParam(value = "range", defaultValue = "MONTHLY") StatsRange range) {
        ExpensesStatsResponseDto stats = statsService.getExpensesStats(range);
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
}
