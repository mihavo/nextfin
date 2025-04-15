package com.nextfin.users.service;

import com.nextfin.exceptions.exception.UserNotFoundException;
import com.nextfin.users.dto.CreateUserDto;
import com.nextfin.users.entity.User;
import com.nextfin.users.service.impl.NextfinUserDetailsService;

public interface UserService extends NextfinUserDetailsService {

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

	/**
	 * Should return the currently authenticated user
	 * @return the current user
	 */
	User getCurrentUser() throws UserNotFoundException;

	/**
	 * Saves current authenticated user's accounts in cache for future reference
	 */
	void cacheAccounts();
}
