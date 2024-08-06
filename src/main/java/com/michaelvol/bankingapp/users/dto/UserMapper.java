package com.michaelvol.bankingapp.users.dto;

import com.michaelvol.bankingapp.users.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

	User toUser(CreateUserDto userDto);
}
