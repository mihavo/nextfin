package com.nextfin.organization.service.impl;

import com.nextfin.account.entity.Account;
import com.nextfin.account.enums.AccountType;
import com.nextfin.account.service.core.AccountService;
import com.nextfin.exceptions.exception.BadRequestException;
import com.nextfin.exceptions.exception.NotFoundException;
import com.nextfin.holder.entity.Holder;
import com.nextfin.organization.dto.CreateOrganizationDto;
import com.nextfin.organization.dto.OrganizationMapper;
import com.nextfin.organization.entity.Organization;
import com.nextfin.organization.repository.OrganizationRepository;
import com.nextfin.organization.service.OrganizationService;
import com.nextfin.organization.validator.OrganizationValidator;
import com.nextfin.users.entity.User;
import com.nextfin.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
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

    @Override
    public Organization getOrganizationByAccountId(Long targetAccountId) {
        Account account = accountService.getAccount(targetAccountId);
        if (account == null) {
            throw new NotFoundException(messageSource.getMessage("account.notfound", new String[]{targetAccountId.toString()},
                                                                 LocaleContextHolder.getLocale()));
        }
        Holder holder = account.getHolder();
        if (holder == null) {
            throw new NotFoundException(messageSource.getMessage("holder.notfound", new String[]{targetAccountId.toString()},
                                                                 LocaleContextHolder.getLocale()));
        }
        User user = holder.getUser();
        return organizationRepository.getByOwner(user).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("organization.not.found", null, LocaleContextHolder.getLocale())));
    }
}
