package com.nextfin.onboarding.repository;

import com.nextfin.onboarding.entity.Tos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TosRepository extends JpaRepository<Tos, UUID> {
}
