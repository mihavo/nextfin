package com.nextfin.transaction.entity;

import com.nextfin.account.entity.Account;
import com.nextfin.holder.entity.Holder;
import com.nextfin.transaction.enums.TransactionStatus;
import com.nextfin.transaction.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Optional;
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
    @JoinColumn(name = "source_account_id", insertable = false, updatable = false, nullable = false)
    private Account sourceAccount;

    @Column(name = "source_account_id", nullable = false)
    private Long sourceAccountId;

    @ManyToOne
    @JoinColumn(name = "target_account_id", insertable = false, updatable = false, nullable = false)
    private Account targetAccount;

    @Transient
    private String targetName;

    private TransactionCategory category = TransactionCategory.TRANSFERS;

    @Column(name = "target_account_id", nullable = false)
    private Long targetAccountId;
    
    @Enumerated
    @Column(name = "transaction_status", nullable = false)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.CREATED;

    @Enumerated
    @Column(name = "transaction_type", nullable = false)
    @Builder.Default
    private TransactionType type = TransactionType.INSTANT;

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

    public String getTargetName() {
        return Optional.ofNullable(targetAccount).map(Account::getHolder).map(Holder::getFullName).orElse(null);
    }

}
