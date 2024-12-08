package com.michaelvol.bankingapp.organization.bill.entity;


import com.michaelvol.bankingapp.organization.bill.enums.BillStatus;
import com.michaelvol.bankingapp.organization.bill.plan.entity.BillPlan;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "bills")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "issue_date", nullable = false)
    private Instant issueDate;

    @Column(name = "due_date", nullable = false)
    private Instant dueDate;

    @Column(name = "status", nullable = false)
    private BillStatus status;

    @ManyToOne
    @JoinColumn(name = "bill_plan_id")
    private BillPlan billPlan;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
