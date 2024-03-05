package com.michaelvol.bankingapp.account.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.michaelvol.bankingapp.account.enums.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Represents the Account entity that stores the user's bank accounts
 */
@Entity
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
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

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
