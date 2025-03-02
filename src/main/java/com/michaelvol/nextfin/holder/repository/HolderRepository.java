package com.michaelvol.nextfin.holder.repository;

import com.michaelvol.nextfin.holder.entity.Holder;
import com.michaelvol.nextfin.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HolderRepository extends JpaRepository<Holder, UUID> {

    boolean existsByUser(User user);

    Optional<Holder> getByUser(User user);
}
