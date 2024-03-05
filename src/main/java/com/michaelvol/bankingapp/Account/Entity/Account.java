package com.michaelvol.bankingapp.Account.Entity;

import com.michaelvol.bankingapp.Account.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

/**
 * Represents the Account entity that stores the user's bank accounts
 */
@Entity
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @SequenceGenerator(name = "accounts_gen", sequenceName = "accounts_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_gen")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Enumerated
    private AccountStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;


    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
