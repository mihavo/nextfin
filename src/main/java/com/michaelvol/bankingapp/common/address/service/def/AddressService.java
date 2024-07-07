package com.michaelvol.bankingapp.common.address.service.def;

import com.michaelvol.bankingapp.common.address.dto.AddressDataDto;
import com.michaelvol.bankingapp.common.address.entity.Address;

/**
 * Interface for common address operations
 */
public interface AddressService {

    public Address create(AddressDataDto dto);

    public Address update(AddressDataDto dto);

    public void delete(Long addressId);
}
