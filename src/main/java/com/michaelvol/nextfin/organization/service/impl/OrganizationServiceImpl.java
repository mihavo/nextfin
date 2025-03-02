package com.michaelvol.nextfin.organization.service.impl;

import com.michaelvol.nextfin.account.entity.Account;
import com.michaelvol.nextfin.account.enums.AccountType;
import com.michaelvol.nextfin.account.service.core.AccountService;
import com.michaelvol.nextfin.exceptions.exception.BadRequestException;
import com.michaelvol.nextfin.exceptions.exception.NotFoundException;
import com.michaelvol.nextfin.organization.dto.CreateOrganizationDto;
import com.michaelvol.nextfin.organization.dto.OrganizationMapper;
import com.michaelvol.nextfin.organization.entity.Organization;
import com.michaelvol.nextfin.organization.repository.OrganizationRepository;
import com.michaelvol.nextfin.organization.service.OrganizationService;
import com.michaelvol.nextfin.organization.validator.OrganizationValidator;
import com.michaelvol.nextfin.users.entity.User;
import com.michaelvol.nextfin.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final OrganizationValidator organizationValidator;
    private final OrganizationMapper organizationMapper;

    private final AccountService accountService;
    private final UserService userService;

    private MessageSource messageSource;

    @Override
    public Organization createOrganization(CreateOrganizationDto dto, User owner) {
        boolean organizationExists = organizationRepository.existsByOwner(owner); 
        if(organizationExists)  {
            throw new BadRequestException("Organization already exists for user");
        }
        Organization organization = organizationMapper.toOrganization(dto);
        return organizationRepository.save(organization);
    }

    @Override
    public Optional<Organization> getOrganizationById(UUID organizationId) throws NoSuchElementException {
        return organizationRepository.findById(organizationId);
    }

    @Override
    public Organization getOrganizationByCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return organizationRepository.getByOwner(currentUser).orElseThrow(() -> new NotFoundException(messageSource.getMessage("organization.not.found", null, Locale.getDefault())));
    }

    @Override
    public List<Account> getAccounts(AccountType type) {
        Organization organization = getOrganizationByCurrentUser();
        return accountService.getAccountsByUser(organization.getOwner(), type);
    }

    @Override
    public void organizationExists(UUID organizationId) throws NoSuchElementException {
        if (!organizationRepository.existsById(organizationId)) {
            throw new NotFoundException(messageSource.getMessage("organization.not.found", null, Locale.getDefault()));
        }
    }
}
