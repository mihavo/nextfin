package com.michaelvol.bankingapp.common.address.entity;

import com.michaelvol.bankingapp.common.address.enums.AddressType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
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

    @Column(name = "zip_code", nullable = false)
    @Range(min = 5, max = 5)
    public String zipCode;

    @Column(name = "type", nullable = false)
    @Builder.Default
    @Enumerated
    public AddressType type = AddressType.BILLING;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    public LocalDate creationDate;

    @UpdateTimestamp
    @Column(name = "updated_date", nullable = false)
    public LocalDate updatedDate;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(street);
        sb.append(" ");
        sb.append(number);
        if (floor != 0) {
            sb.append(" ");
            sb.append(floor);
            sb.append(" floor");
        }
        sb.append(", ");
        sb.append(city);
        sb.append(" ");
        sb.append(state);
        sb.append(" ");
        sb.append(zipCode);
        return sb.toString();
    }
}
