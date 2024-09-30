package com.michaelvol.bankingapp.holder.repository;

import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HolderRepository extends JpaRepository<Holder, UUID> {

    boolean existsByUser(User user);

    Optional<Holder> getByUser(User user);
}
