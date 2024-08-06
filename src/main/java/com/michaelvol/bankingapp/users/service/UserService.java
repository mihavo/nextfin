package com.michaelvol.bankingapp.users.service;

import com.michaelvol.bankingapp.users.dto.CreateUserDto;
import com.michaelvol.bankingapp.users.entity.User;

public interface UserService {

	/**
	 * Should create a new user with the given information
	 *
	 * @param dto
	 * the information to create the user with
	 * @return the created user
	 */
	User createUser(CreateUserDto dto);
}
