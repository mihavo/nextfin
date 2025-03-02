package com.michaelvol.nextfin.organization.service;

import com.michaelvol.nextfin.account.entity.Account;
import com.michaelvol.nextfin.account.enums.AccountType;
import com.michaelvol.nextfin.organization.dto.CreateOrganizationDto;
import com.michaelvol.nextfin.organization.entity.Organization;
import com.michaelvol.nextfin.users.entity.User;

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
