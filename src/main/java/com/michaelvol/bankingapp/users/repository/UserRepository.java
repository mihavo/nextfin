package com.michaelvol.bankingapp.users.repository;

import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

	boolean existsBySocialSecurityNumber(String socialSecurityNumber);

	boolean existsByHolder(Holder holder);

	Optional<User> findByUsername(String username);
}
