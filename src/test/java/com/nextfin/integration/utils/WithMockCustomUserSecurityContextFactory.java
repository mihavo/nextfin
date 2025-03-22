package com.nextfin.integration.utils;

import com.nextfin.holder.entity.Holder;
import com.nextfin.users.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDate;
import java.util.UUID;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockNextfinUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockNextfinUser user) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User nextfinUser = User.builder().username(user.username()).hashedPassword("mockpasswd").id(UUID.randomUUID()).email(
                user.email()).build();
        //Integration Tests will be required to persist the holder before any operations of the holder's foreign keys 
        Holder holder = Holder.builder().firstName("John").lastName("Doe").dateOfBirth(LocalDate.of(1990, 5, 15)).phoneNumber(
                "1234567890").build();
        nextfinUser.setHolder(holder);
        Authentication auth = new UsernamePasswordAuthenticationToken(nextfinUser, "mockpasswd", nextfinUser.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}