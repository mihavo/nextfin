package com.michaelvol.bankingapp.users.repository;

import java.util.UUID;

import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

	boolean existsBySocialSecurityNumber(String socialSecurityNumber);

	boolean existsByHolder(Holder holder);
}
