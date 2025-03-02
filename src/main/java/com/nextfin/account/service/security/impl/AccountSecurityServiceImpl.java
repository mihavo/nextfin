package com.nextfin.account.service.security.impl;

import com.nextfin.account.repository.AccountRepository;
import com.nextfin.account.service.security.AccountSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountSecurityServiceImpl implements AccountSecurityService {


    private final AccountRepository accountRepository;

    @Override
    public boolean isAccountOwner(Long accountId) {
        if (accountId == null || accountId <= 0) {
            return false;
        }
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userIdOfAccount = accountRepository.findUserIdByAccountId(accountId);
        
    }
}
