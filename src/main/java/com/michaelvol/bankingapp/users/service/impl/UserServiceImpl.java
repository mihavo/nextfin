package com.michaelvol.bankingapp.users.service.impl;

import com.michaelvol.bankingapp.exceptions.exception.BadRequestException;
import com.michaelvol.bankingapp.exceptions.exception.UserNotFoundException;
import com.michaelvol.bankingapp.users.dto.CreateUserDto;
import com.michaelvol.bankingapp.users.dto.UserMapper;
import com.michaelvol.bankingapp.users.entity.User;
import com.michaelvol.bankingapp.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends CustomUserDetailsService implements UserService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;


	@Override
	public User createUser(CreateUserDto dto) {
		boolean userMatch = userRepository.existsBySocialSecurityNumber(dto.getSocialSecurityNumber());
		if (userMatch) {
			throw new BadRequestException(messageSource.getMessage("user.exists", null, LocaleContextHolder.getLocale()));
		}
		User user = userMapper.toUser(dto);
		String encodedPassword = passwordEncoder.encode(dto.getPassword());
		user.setHashedPassword(encodedPassword);
		return userRepository.save(user);
	}


	@Override
	public User getCurrentUser() {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (currentUser == null) {
			throw new UserNotFoundException(messageSource.getMessage("user.not-found",
																	 new String[]{"current user"},
																	 LocaleContextHolder.getLocale()));
		}
		return currentUser;
	}
}
