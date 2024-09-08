package com.michaelvol.bankingapp.auth.oauth2.service;

import com.michaelvol.bankingapp.users.dto.CreateUserDto;
import com.michaelvol.bankingapp.users.dto.UserMapper;
import com.michaelvol.bankingapp.users.entity.User;
import com.michaelvol.bankingapp.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OidcService extends OidcUserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        log.trace("Received OpenID ID token: {}", oidcUser.getIdToken());
        Optional<User> byUsername = userRepository.findByUsername(oidcUser.getPreferredUsername());
        if (byUsername.isPresent()) {
            updateUser(byUsername.get(), oidcUser);
        } else {
            registerUser(oidcUser);
        }
        return oidcUser;
    }

    private void registerUser(OidcUser oidcUser) {
        log.info("User {} does not exist, registering user", oidcUser.getPreferredUsername());
        CreateUserDto userDto = CreateUserDto.builder()
                .email(oidcUser.getEmail())
                .username(oidcUser.getPreferredUsername())
                .build();
        userRepository.save(userMapper.toUser(userDto));
    }

    private void updateUser(User existingUser, OidcUser oidcUser) {
        log.info("User {} already exists, updating user details", existingUser.getUsername());
        existingUser.setUsername(oidcUser.getPreferredUsername());
        existingUser.setEmail(oidcUser.getEmail());
        userRepository.save(existingUser);
    }
}
