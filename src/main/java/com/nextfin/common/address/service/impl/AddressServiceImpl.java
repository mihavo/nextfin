package com.nextfin.common.address.service.impl;

import com.nextfin.common.address.dto.AddressDataDto;
import com.nextfin.common.address.entity.Address;
import com.nextfin.common.address.enums.AddressType;
import com.nextfin.common.address.enums.Floor;
import com.nextfin.common.address.repository.AddressRepository;
import com.nextfin.common.address.service.def.AddressService;
import com.nextfin.exceptions.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * Main implementation of {@link AddressService}
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final MessageSource messageSource;

    @Override
    public Address create(AddressDataDto dto) {
        String street = dto.street();
        Integer number = dto.number();
        Floor floor = dto.floor();
        String city = dto.city();
        String zipCode = dto.zipCode();

        Address address = addressRepository.findAddress(street, number, floor, city, zipCode);
        if (address != null)
            return address;
        return addressRepository.save(Address.builder()
                                             .street(street)
                                             .number(number)
                                             .floor(floor != null ? floor : Floor.GROUND)
                                             .city(city)
                                             .zipCode(zipCode)
                                             .state(dto.state())
                                             .type(dto.addressType() != null ? dto.addressType() : AddressType.BILLING)
                                             .build());
    }

    @Override
    public Address update(AddressDataDto dto) {
        //TODO: Implement
        return null;
    }

    @Override
    public void delete(Long addressId) {
        addressRepository.findById(addressId)
                         .orElseThrow(() -> new BadRequestException(messageSource.getMessage("address.notfound",
                                                                                             new Long[]{addressId},
                                                                                             LocaleContextHolder.getLocale())));
    }
}
