package com.michaelvol.bankingapp.transaction.entity;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import com.michaelvol.bankingapp.transaction.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Transaction {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.AUTO)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id", nullable = false)
    private Account targetAccount;

    @Enumerated
    @Column(name = "transaction_status", nullable = false)
    @Builder.Default
    private TransactionStatus transactionStatus = TransactionStatus.CREATED;

    @Enumerated
    @Column(name = "transaction_type", nullable = false)
    @Builder.Default
    private TransactionType transactionType = TransactionType.INSTANT;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "fee", nullable = false)
    @Builder.Default
    private BigDecimal fee = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}
