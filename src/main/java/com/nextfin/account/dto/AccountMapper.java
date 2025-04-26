package com.nextfin.account.dto;

import com.nextfin.account.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "holder.id", target = "holderId")
    CreateAccountResponseDto toCreateAccountResponseDto(Account account);
}
    