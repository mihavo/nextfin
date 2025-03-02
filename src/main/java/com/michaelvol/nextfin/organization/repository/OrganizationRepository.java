package com.michaelvol.nextfin.organization.repository;

import com.michaelvol.nextfin.organization.entity.Organization;
import com.michaelvol.nextfin.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByOwner(User owner);

    Optional<Organization> getByOwner(User owner);
} 
