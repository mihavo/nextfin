package com.nextfin.holder.service.impl;

import com.nextfin.account.entity.Account;
import com.nextfin.account.enums.AccountType;
import com.nextfin.common.address.dto.AddressDataDto;
import com.nextfin.common.address.entity.Address;
import com.nextfin.common.address.service.def.AddressService;
import com.nextfin.exceptions.exception.BadRequestException;
import com.nextfin.exceptions.exception.NotFoundException;
import com.nextfin.exceptions.exception.UserNotFoundException;
import com.nextfin.holder.dto.CreateHolderDto;
import com.nextfin.holder.dto.HolderMapper;
import com.nextfin.holder.entity.Holder;
import com.nextfin.holder.repository.HolderRepository;
import com.nextfin.holder.service.HolderService;
import com.nextfin.users.entity.User;
import com.nextfin.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolderServiceImpl implements HolderService {

    private final HolderRepository holderRepository;

    private final AddressService addressService;

    private final HolderMapper mapper;

    private final MessageSource messageSource;
    private final UserRepository userRepository;

    @Override
    public Holder createHolder(CreateHolderDto dto, User user) {
        boolean holderMatch = holderRepository.existsByUser(user);
        if (holderMatch) {
            throw new BadRequestException(messageSource.getMessage("holder.exists",
                                                                   null,
                                                                   LocaleContextHolder.getLocale()));
        }
        AddressDataDto addressData = dto.getAddress();

        //The address service will check if an address with the same address
        //data already exists, returning the existing one in that case
        Address address = addressService.create(addressData);

        Holder holder = mapper.createHolderDtoToHolder(dto);
        holder.setAddress(address);
        holder.setUser(user);
        user.setHolder(holder);
        holder = holderRepository.save(holder);
        userRepository.save(user);
        return holder;
    }

    @Override
    public List<Account> getAccounts(AccountType type) {
        List<Account> accounts = getHolderByCurrentUser().getAccounts();
        return type == null ? accounts : accounts.stream().filter(account -> account.getAccountType().equals(type)).toList();
    }

    @Override
    public Holder getHolderById(UUID holderId) throws NoSuchElementException {
        return holderRepository.findById(holderId)
                               .orElseThrow(() -> new NotFoundException("Holder with id " + holderId + " not found"));
    }

    @Override
    public Holder getHolderByCurrentUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof OidcUser oidcUser) {
            user = userRepository.findUserByEmail((oidcUser.getEmail())).orElseThrow(() -> new UserNotFoundException(
                    messageSource.getMessage("unknown_user.not-found", null, LocaleContextHolder.getLocale())));
        }
        return holderRepository.getByUser((User) user)
                               .orElseThrow(() -> new NotFoundException("Holder not found for current user"));
    }

}
