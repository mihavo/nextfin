package com.michaelvol.bankingapp.account.service.security;

public interface AccountSecurityService {

    boolean isAccountOwner(Long accountId);
}
