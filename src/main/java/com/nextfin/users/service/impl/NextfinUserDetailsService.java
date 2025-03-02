package com.nextfin.users.service.impl;

import com.nextfin.exceptions.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface NextfinUserDetailsService extends UserDetailsService {

    UserDetails loadUserById(UUID id) throws UserNotFoundException;

}
