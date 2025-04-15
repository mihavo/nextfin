package com.nextfin.account.service.scheduler;

import com.nextfin.account.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountScheduler {
    private final AccountRepository accountRepository;

    @Scheduled(cron = "0 0 0 * * *") // Every midnight
    @Transactional
    public void resetDailyTotals() {
        accountRepository.resetDailyTotals();
    }
}
