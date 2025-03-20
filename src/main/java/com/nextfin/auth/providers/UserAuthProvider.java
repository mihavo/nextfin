package com.nextfin.auth.providers;

import com.nextfin.users.entity.NextfinUserDetails;
import com.nextfin.users.service.impl.NextfinUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
public class UserAuthProvider implements AuthenticationProvider {

    private final NextfinUserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        NextfinUserDetails userDetails = (NextfinUserDetails) userService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException(messageSource.getMessage("auth.bad-credentials",
                                                                       null,
                                                                       LocaleContextHolder.getLocale()));
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
