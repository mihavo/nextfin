package com.nextfin.organization.repository;

import com.nextfin.organization.entity.Organization;
import com.nextfin.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByOwner(User owner);

    Optional<Organization> getByOwner(User owner);
} 
