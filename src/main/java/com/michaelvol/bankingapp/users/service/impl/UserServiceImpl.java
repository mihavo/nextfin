package com.michaelvol.bankingapp.users.service.impl;

import com.michaelvol.bankingapp.exceptions.exception.BadRequestException;
import com.michaelvol.bankingapp.users.dto.CreateUserDto;
import com.michaelvol.bankingapp.users.dto.UserMapper;
import com.michaelvol.bankingapp.users.entity.User;
import com.michaelvol.bankingapp.users.repository.UserRepository;
import com.michaelvol.bankingapp.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private final MessageSource messageSource;

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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
							 .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("user.not-found",
																									   null,
																									   LocaleContextHolder.getLocale())));
	}
}
