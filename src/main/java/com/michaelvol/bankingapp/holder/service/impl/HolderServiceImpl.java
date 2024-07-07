package com.michaelvol.bankingapp.holder.service.impl;

import com.michaelvol.bankingapp.common.address.dto.AddressDataDto;
import com.michaelvol.bankingapp.common.address.entity.Address;
import com.michaelvol.bankingapp.common.address.service.def.AddressService;
import com.michaelvol.bankingapp.exceptions.exception.BadRequestException;
import com.michaelvol.bankingapp.exceptions.exception.NotFoundException;
import com.michaelvol.bankingapp.holder.dto.CreateHolderRequestDto;
import com.michaelvol.bankingapp.holder.dto.HolderMapper;
import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.holder.repository.HolderRepository;
import com.michaelvol.bankingapp.holder.service.HolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class HolderServiceImpl implements HolderService {

    private final HolderRepository holderRepository;

    private final AddressService addressService;

    private final HolderMapper mapper;

    private final MessageSource messageSource;

    @Override
    public Holder createHolder(CreateHolderRequestDto dto) {
        boolean holderMatch = holderRepository.existsHolderBySocialSecurityNumber(dto.getSocialSecurityNumber());
        if (holderMatch) {
            throw new BadRequestException(messageSource.getMessage("holder.exists",
                                                                   null,
                                                                   LocaleContextHolder.getLocale()));
        }
        AddressDataDto addressData = dto.getAddressData();

        //The address service will check if an address with the same address
        //data already exists, returning the existing one in that case
        Address address = addressService.create(addressData);

        Holder holder = mapper.createHolderRequestDtoToHolder(dto);
        holder.setAddress(address);
        return holderRepository.save(holder);
    }

    @Override
    public Holder getHolderById(Long holderId) throws NoSuchElementException {
        return holderRepository.findById(holderId)
                               .orElseThrow(() -> new NotFoundException("Holder with id " + holderId + " not found"));
    }
}
