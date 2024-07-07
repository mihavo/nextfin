package com.michaelvol.bankingapp.common.address.dto;

import com.michaelvol.bankingapp.common.address.enums.AddressType;

public record AddressDataDto(String street,
                             Integer number,
                             Integer floor,
                             String city,
                             String state,
                             String zipCode,
                             AddressType addressType
) {
}
