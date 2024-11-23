package com.michaelvol.bankingapp.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.michaelvol.bankingapp.account.enums.AccountStatus;
import com.michaelvol.bankingapp.account.enums.AccountType;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.holder.entity.Holder;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

/**
 * Represents the Account entity that stores the user's bank accounts
 */
@Entity
@DynamicUpdate
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
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Enumerated
    private AccountStatus status;

    @Enumerated
    @Builder.Default
    private AccountType accountType = AccountType.SAVINGS;

    @ManyToOne
    @JoinColumn(name = "holder_id")
    @JsonIgnore
    private Holder holder;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @Column(name = "transaction_limit")
    @Builder.Default
    Long transactionLimit = 1000L;

    @Column(name = "transaction_limit_enabled", nullable = false)
    @Builder.Default
    Boolean transactionLimitEnabled = true;

    @Column(name = "transaction_2fa_enabled")
    @Builder.Default
    Boolean transaction2FAEnabled = true;

    @Column(name = "transaction_sms_confirmation_enabled")
    @Builder.Default
    Boolean transactionSMSConfirmationEnabled = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant dateOpened;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastUpdated;
}
