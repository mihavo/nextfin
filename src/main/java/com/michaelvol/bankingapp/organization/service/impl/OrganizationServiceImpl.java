package com.michaelvol.bankingapp.organization.service.impl;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.enums.AccountType;
import com.michaelvol.bankingapp.account.service.core.AccountService;
import com.michaelvol.bankingapp.exceptions.exception.BadRequestException;
import com.michaelvol.bankingapp.exceptions.exception.NotFoundException;
import com.michaelvol.bankingapp.organization.bill.plan.repository.BillPlanRepository;
import com.michaelvol.bankingapp.organization.dto.CreateOrganizationDto;
import com.michaelvol.bankingapp.organization.dto.OrganizationMapper;
import com.michaelvol.bankingapp.organization.entity.Organization;
import com.michaelvol.bankingapp.organization.repository.OrganizationRepository;
import com.michaelvol.bankingapp.organization.service.OrganizationService;
import com.michaelvol.bankingapp.organization.validator.OrganizationValidator;
import com.michaelvol.bankingapp.users.entity.User;
import com.michaelvol.bankingapp.users.service.UserService;
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
    private final BillPlanRepository billPlanRepository;

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
