package com.nextfin.users.repository;

import com.nextfin.holder.entity.Holder;
import com.nextfin.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

	boolean existsBySocialSecurityNumber(String socialSecurityNumber);

	boolean existsByHolder(Holder holder);

	Optional<User> findByUsername(String username);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Optional<UUID> getIdByEmail(String email);
}
