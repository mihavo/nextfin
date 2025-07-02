package com.nextfin.stats.service;

import com.nextfin.stats.dto.ExpensesStatsResponseDto;
import com.nextfin.stats.dto.IncomeStatsResponseDto;
import com.nextfin.stats.enums.StatsRange;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.repository.TransactionRepository;
import com.nextfin.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public IncomeStatsResponseDto getIncomeStats(StatsRange range) {
        ZoneId zone = ZoneId.systemDefault();
        Instant now = Instant.now();
        ZonedDateTime start = getStartTime(range, now, zone);

        List<Transaction> incomeTxs = transactionRepository.findIncomeByTimePeriod(
                userService.getCurrentUser().getHolder().getId(), start.toInstant(), now);

        BigDecimal total = calculateTotal(incomeTxs);
        BigDecimal avg = calculateAverage(incomeTxs, total);
        return IncomeStatsResponseDto.builder().totalIncome(total).averageIncome(avg).startDate(
                LocalDate.ofInstant(start.toInstant(), zone)).endDate(now.atZone(zone).toLocalDate()).build();
    }

    public ExpensesStatsResponseDto getExpensesStats(StatsRange range) {
        ZoneId zone = ZoneId.systemDefault();
        Instant now = Instant.now();
        ZonedDateTime start = getStartTime(range, now, zone);

        List<Transaction> expenseTxs = transactionRepository.findExpensesByTimePeriod(
                userService.getCurrentUser().getHolder().getId(), start.toInstant(), now);

        BigDecimal total = calculateTotal(expenseTxs);
        BigDecimal avg = calculateAverage(expenseTxs, total);
        return ExpensesStatsResponseDto.builder().totalExpenses(total).averageExpense(avg).startDate(
                LocalDate.ofInstant(start.toInstant(), zone)).endDate(now.atZone(zone).toLocalDate()).build();
    }

    private static @NotNull BigDecimal calculateAverage(List<Transaction> incomeTxs, BigDecimal total) {
        return incomeTxs.isEmpty() ? BigDecimal.ZERO : total.divide(BigDecimal.valueOf(incomeTxs.size()), RoundingMode.HALF_UP);
    }

    private static @NotNull BigDecimal calculateTotal(List<Transaction> transactions) {
        return transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static @NotNull ZonedDateTime getStartTime(StatsRange range, Instant now, ZoneId zone) {
        ZonedDateTime zonedNow = now.atZone(zone);
        return switch (range) {
            case DAILY -> zonedNow.minusDays(1);
            case WEEKLY -> zonedNow.minusWeeks(1);
            case MONTHLY -> zonedNow.minusMonths(1);
            case YEARLY -> zonedNow.minusYears(1);
        };
    }

}
