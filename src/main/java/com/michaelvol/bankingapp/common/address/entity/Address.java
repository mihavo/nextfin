package com.michaelvol.bankingapp.common.address.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
@Table(name = "address")
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long id;

    @Column(name = "street", nullable = false)
    public String street;

    @Column(name = "number", nullable = false)
    @Positive
    public Integer number;

    @Builder.Default
    public Integer floor = 0;

    @Column(name = "city", nullable = false)
    public String city;

    @Column(name = "state", nullable = false)
    public String state;

    @Column(name = "zip", nullable = false)
    @Range(min = 5, max = 5)
    public String zip;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    public LocalDate creationDate;

    @UpdateTimestamp
    @Column(name = "updated_date", nullable = false)
    public LocalDate updatedDate;
}
