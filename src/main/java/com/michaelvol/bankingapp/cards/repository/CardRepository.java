package com.michaelvol.bankingapp.cards.repository;

import com.michaelvol.bankingapp.cards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    Boolean existsCardByCardNumber(String cardNumber);
}
