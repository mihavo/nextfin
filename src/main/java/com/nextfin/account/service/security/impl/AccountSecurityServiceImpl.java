package com.nextfin.account.service.security.impl;

import com.nextfin.account.repository.AccountRepository;
import com.nextfin.account.service.security.AccountSecurityService;
import com.nextfin.users.entity.NextfinUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountSecurityServiceImpl implements AccountSecurityService {
    
    private final AccountRepository accountRepository;

    @Override
    public boolean isAccountOwner(Long accountId) {
        if (accountId == null || accountId <= 0) {
            return false;
        }
        // Get authentication safely
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof NextfinUserDetails currentUser)) {
            return false;
        }
        UUID accountUserId = accountRepository.findUserIdByAccountId(accountId);
        if (accountUserId == null) {
            return false;
        }
        return currentUser.getId().equals(accountUserId);
    }
}
