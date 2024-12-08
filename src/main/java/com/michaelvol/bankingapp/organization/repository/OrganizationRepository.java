package com.michaelvol.bankingapp.organization.repository;

import com.michaelvol.bankingapp.organization.entity.Organization;
import com.michaelvol.bankingapp.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByOwner(User owner);

    Optional<Organization> getByOwner(User owner);
} 
