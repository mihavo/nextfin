package com.michaelvol.bankingapp.organization.service.impl;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.account.enums.AccountType;
import com.michaelvol.bankingapp.account.repository.AccountRepository;
import com.michaelvol.bankingapp.exceptions.exception.BadRequestException;
import com.michaelvol.bankingapp.exceptions.exception.NotFoundException;
import com.michaelvol.bankingapp.organization.dto.CreateOrganizationDto;
import com.michaelvol.bankingapp.organization.dto.OrganizationMapper;
import com.michaelvol.bankingapp.organization.entity.Organization;
import com.michaelvol.bankingapp.organization.repository.OrganizationRepository;
import com.michaelvol.bankingapp.organization.service.OrganizationService;
import com.michaelvol.bankingapp.organization.validator.OrganizationValidator;
import com.michaelvol.bankingapp.users.entity.User;
import com.michaelvol.bankingapp.users.service.UserService;
import com.michaelvol.bankingapp.users.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationValidator organizationValidator;
    private final OrganizationMapper organizationMapper;
    private final AccountRepository accountRepository;
    private final UserService userService;

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
//         organizationRepository.getByOwner(currentUser)

    }

    @Override
    public List<Account> getAccounts(AccountType type) {
        return List.of();
    }

    @Override
    public void organizationExists(UUID organizationId) throws NoSuchElementException {
        if (!organizationRepository.existsById(organizationId)) {
            throw new NotFoundException()
        }
    }
}
