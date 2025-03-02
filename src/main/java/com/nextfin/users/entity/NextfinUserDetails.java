package com.nextfin.users.entity;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface NextfinUserDetails extends UserDetails {

    UUID getId();
}
