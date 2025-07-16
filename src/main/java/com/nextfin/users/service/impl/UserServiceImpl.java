package com.nextfin.users.service.impl;

import com.nextfin.account.entity.Account;
import com.nextfin.account.service.core.AccountService;
import com.nextfin.cache.CacheService;
import com.nextfin.cache.utils.CacheUtils;
import com.nextfin.exceptions.exception.BadRequestException;
import com.nextfin.exceptions.exception.ForbiddenException;
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
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final UserRepository userRepository;
	private final MessageSource messageSource;
	private final PasswordEncoder passwordEncoder;
	private final AccountService accountService;
	private final CacheService cache;


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
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
	public User getCurrentUser() throws UserNotFoundException {
        Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (currentUser == null) {
			throw new ForbiddenException();
		}
        if (currentUser instanceof OidcUser oidcUser) {
            return userRepository.findUserByEmail((oidcUser.getEmail())).orElseThrow(() -> new UserNotFoundException(
                    messageSource.getMessage("unknown_user.not-found", null, LocaleContextHolder.getLocale())));
        } else return (User) currentUser;
	}

	@Override
	public void cacheAccounts() {
		User user = getCurrentUser();
		List<Account> accounts = accountService.getCurrentUserAccounts();
		String cacheKey = CacheUtils.buildAccountsSetKey(user.getId());
		accounts.forEach(account -> cache.addToSortedSet(cacheKey, account.getId().toString(),
														 account.getDateOpened().toEpochMilli() / 1000.0));
	}

    @Override
    public Optional<UUID> gettUserIdByEmail(String email) {
        return userRepository.getIdByEmail(email);
    }

    @Override
    public boolean isOnboardingComplete(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    @Override
	public UserDetails loadUserById(UUID id) throws UserNotFoundException {
		return userRepository.findById(id)
							 .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("user.not-found-by-id",
																								   new String[]{id.toString()},
									 LocaleContextHolder.getLocale())));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
							 .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage(
									 "user.not-found", new String[]{username},
									 LocaleContextHolder.getLocale())));
	}
}
