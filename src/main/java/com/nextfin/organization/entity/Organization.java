package com.nextfin.organization.entity;

import com.nextfin.common.address.entity.Address;
import com.nextfin.organization.enums.IndustryType;
import com.nextfin.organization.enums.OrganizationLegalType;
import com.nextfin.users.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "organizations")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Column(name = "tax_id", nullable = false)
    private String taxId;

    @Column(name = "legal_type", nullable = false)
    private OrganizationLegalType legalType;

    @Column(name = "industry_type", nullable = false)
    private IndustryType industryType;

    @ManyToOne
    @JoinColumn(name = "operational_address_id")
    private Address operationalAddress;

    @ManyToOne
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
