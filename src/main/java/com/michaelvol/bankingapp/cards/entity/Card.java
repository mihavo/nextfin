package com.michaelvol.bankingapp.cards.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.cards.enums.CardStatus;
import com.michaelvol.bankingapp.cards.enums.CardType;
import com.michaelvol.bankingapp.common.address.entity.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "cards")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "card_number", nullable = false, unique = true)
    @CreditCardNumber(ignoreNonDigitCharacters = true)
    @Pattern(regexp = AppConstants.VISA_REGEX)
    private String cardNumber;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "cvv", nullable = false)
    @JsonIgnore
    private Long cvv;

    @Column(name = "cardholder_name", nullable = false)
    @Length(min = 2, max = 26)
    private String cardholderName;

    @ManyToOne
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @Column(name = "type", nullable = false)
    @Enumerated
    private CardType type;

    @Column(name = "status", nullable = false)
    @Enumerated
    private CardStatus status;

    @CreationTimestamp
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

}
