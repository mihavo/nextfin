package com.nextfin.cards.repository;

import com.nextfin.cards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    Boolean existsCardByCardNumber(String cardNumber);
}
