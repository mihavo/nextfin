package com.michaelvol.bankingapp.transaction.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.transaction.enums.TransactionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

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
    @SequenceGenerator(name = "transactions_gen", sequenceName = "transactions_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_gen")
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    @OneToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;

    @OneToOne
    @JoinColumn(name = "target_account_id", nullable = false)
    private Account targetAccount;

    @Enumerated
    @Column(name = "transaction_status", nullable = false)
    @Builder.Default
    private TransactionStatus transactionStatus = TransactionStatus.PENDING;

    @Column(name = "fee", nullable = false)
    @Builder.Default
    private BigDecimal fee = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
