package com.nextfin.organization.service;

import com.nextfin.account.entity.Account;
import com.nextfin.account.enums.AccountType;
import com.nextfin.organization.dto.CreateOrganizationDto;
import com.nextfin.organization.entity.Organization;
import com.nextfin.users.entity.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationService {


    Organization createOrganization(CreateOrganizationDto dto, User owner);

    Optional<Organization> getOrganizationById(UUID organizationId) throws NoSuchElementException;

    Organization getOrganizationByCurrentUser();

    List<Account> getAccounts(AccountType type);

    void organizationExists(UUID organizationId) throws NoSuchElementException;

    Optional<Organization> getOrganizationByAccountId(Long targetAccountId);
}
