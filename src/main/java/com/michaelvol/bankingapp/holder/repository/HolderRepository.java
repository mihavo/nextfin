package com.michaelvol.bankingapp.holder.repository;

import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolderRepository extends JpaRepository<Holder, Long> {

    boolean existsByUser(User user);
}
