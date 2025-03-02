package com.nextfin.users.service.impl;

import com.nextfin.exceptions.exception.BadRequestException;
import com.nextfin.exceptions.exception.UserNotFoundException;
import com.nextfin.users.dto.CreateUserDto;
import com.nextfin.users.dto.UserMapper;
import com.nextfin.users.entity.User;
import com.nextfin.users.repository.UserRepository;
import com.nextfin.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final MessageSource messageSource;
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
	public User findUserByUsername(String username) throws UserNotFoundException {
		UserDetails userDetails = loadUserByUsername(username);
		if (!(userDetails instanceof User user)) {
			log.error("Failed to cast UserDetails to User for username: {}", username);
			throw new UserNotFoundException(messageSource.getMessage("user.not-found",
																	 new String[]{"current user"},
																	 LocaleContextHolder.getLocale()));
		}
		return user;
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

	@Override
	public UserDetails loadUserById(UUID id) throws UserNotFoundException {
		return userRepository.findById(id)
							 .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage(
									 "user.not-found",
									 new String[]{"current user"},
									 LocaleContextHolder.getLocale())));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
							 .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage(
									 "user.not-found",
									 new String[]{"current user"},
									 LocaleContextHolder.getLocale())));
	}
}
