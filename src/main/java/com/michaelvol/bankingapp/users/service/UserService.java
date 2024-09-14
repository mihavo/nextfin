package com.michaelvol.bankingapp.users.service;

import com.michaelvol.bankingapp.exceptions.exception.UserNotFoundException;
import com.michaelvol.bankingapp.users.dto.CreateUserDto;
import com.michaelvol.bankingapp.users.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	/**
	 * Should create a new user with the given information
	 *
	 * @param dto
	 * the information to create the user with
	 * @return the created user
	 */
	User createUser(CreateUserDto dto);

	/**
	 * Should fetch an existing user from the DB. If the user doesn't
	 * exist a {@link UserNotFoundException} is thrown
	 * @param username the username to search
	 * @return the user found
	 */
	User findUserByUsername(String username) throws UserNotFoundException;
}
