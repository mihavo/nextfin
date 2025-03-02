package com.michaelvol.bankingapp.users.service.impl;

import com.michaelvol.bankingapp.exceptions.exception.UserNotFoundException;
import com.michaelvol.bankingapp.users.entity.User;
import com.michaelvol.bankingapp.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    protected final UserRepository userRepository;

    protected final MessageSource messageSource;


    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        return (UserDetails) findUserByUsername(username);
    }


    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                             .orElseThrow(() -> new UserNotFoundException(messageSource.getMessage("user.not-found",
                                                                                                   new String[]{username},
                                                                                                   LocaleContextHolder.getLocale())));
    }
}
