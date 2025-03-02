package com.michaelvol.nextfin.holder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.michaelvol.nextfin.account.entity.Account;
import com.michaelvol.nextfin.common.address.entity.Address;
import com.michaelvol.nextfin.generic.Auditable;
import com.michaelvol.nextfin.users.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "holders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Holder extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "first-name", nullable = false)
    private String firstName;

    @Column(name = "last-name", nullable = false)
    private String lastName;

    @Column(name = "date-of-birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone-number", nullable = false)
    @Size(min = 7, max = 15, message = "Phone number should have at least 10 or less than 15 digits")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @JsonIgnore
    private Address address;

    @ToString.Exclude
    @OneToMany(mappedBy = "holder")
    @JsonIgnore
    private List<Account> accounts;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
