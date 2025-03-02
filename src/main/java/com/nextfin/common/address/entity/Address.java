package com.nextfin.common.address.entity;

import com.nextfin.common.address.enums.AddressType;
import com.nextfin.common.address.enums.Floor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "address")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Address implements Serializable {

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
    @Enumerated
    public Floor floor = Floor.GROUND;

    @Column(name = "city", nullable = false)
    public String city;

    @Column(name = "state", nullable = false)
    public String state;

    @Column(name = "zip_code", nullable = false)
    @Length(min = 5, max = 5)
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
        sb.append(" ");
        sb.append(floor);
        sb.append(", ");
        sb.append(city);
        sb.append(" ");
        sb.append(state);
        sb.append(" ");
        sb.append(zipCode);
        return sb.toString();
    }
}
