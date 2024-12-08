package com.michaelvol.bankingapp.organization.service;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.enums.AccountType;
import com.michaelvol.bankingapp.organization.dto.CreateOrganizationDto;
import com.michaelvol.bankingapp.organization.entity.Organization;
import com.michaelvol.bankingapp.users.entity.User;

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
}
