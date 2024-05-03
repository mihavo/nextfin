package com.michaelvol.bankingapp.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.michaelvol.bankingapp.account.enums.AccountStatus;
import com.michaelvol.bankingapp.account.enums.AccountType;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.holder.entity.Holder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Enumerated
    @Builder.Default
    private AccountType accountType = AccountType.SAVINGS;

    @ManyToOne
    @JoinColumn(name = "holder_id")
    private Holder holder;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant dateOpened;

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastUpdated;
}
