package com.nextfin.auth.oauth2.service;

import com.nextfin.users.dto.CreateUserDto;
import com.nextfin.users.dto.UserMapper;
import com.nextfin.users.entity.User;
import com.nextfin.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OidcService extends OidcUserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private String username;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        log.trace("Received OpenID ID token: {}", oidcUser.getIdToken());
        username = Objects.requireNonNullElse(oidcUser.getPreferredUsername(), oidcUser.getEmail());
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            updateUser(byUsername.get(), oidcUser);
        } else {
            registerUser(userRequest, oidcUser);
        }
        return oidcUser;
    }

    private void registerUser(OidcUserRequest userRequest, OidcUser oidcUser) {
        log.info("User {} does not exist, registering user", username);
        CreateUserDto userDto = CreateUserDto.builder()
                                             .email(oidcUser.getEmail())
                                             .username(username)
                                             .build();
        User user = userMapper.toUser(userDto);
        user.setAuthProvider(userRequest.getClientRegistration().getClientId());
        user.setAuthClientName(userRequest.getClientRegistration().getClientName());
        user.setAuthProviderId(oidcUser.getIdToken().getTokenValue());
        userRepository.save(user);
    }

    private void updateUser(User existingUser, OidcUser oidcUser) {
        log.info("User {} already exists, updating user details", existingUser.getUsername());
        existingUser.setUsername(username);
        existingUser.setEmail(oidcUser.getEmail());
        userRepository.save(existingUser);
    }
}
