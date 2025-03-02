package com.michaelvol.nextfin.users.dto;

import com.michaelvol.nextfin.users.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(source = "phoneNumber", target = "preferredPhoneNumber")
	User toUser(CreateUserDto userDto);
}
