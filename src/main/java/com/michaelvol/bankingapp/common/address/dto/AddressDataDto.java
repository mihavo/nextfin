package com.michaelvol.bankingapp.common.address.dto;

import com.michaelvol.bankingapp.common.address.enums.AddressType;
import org.hibernate.validator.constraints.Length;

public record AddressDataDto(String street,
                             Integer number,
                             Integer floor,
                             String city,
                             String state,
                             @Length(min = 5, max = 5) String zipCode,
                             AddressType addressType
) {
}
