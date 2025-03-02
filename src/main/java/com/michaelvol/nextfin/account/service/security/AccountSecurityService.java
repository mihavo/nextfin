package com.michaelvol.nextfin.account.service.security;

public interface AccountSecurityService {

    boolean isAccountOwner(Long accountId);
}
