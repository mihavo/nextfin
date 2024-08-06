package com.michaelvol.bankingapp.holder.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.common.address.entity.Address;
import com.michaelvol.bankingapp.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "holders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Holder {
    @Id
    @SequenceGenerator(name = "sequence_gen", sequenceName = "holders_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_gen")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first-name", nullable = false)
    private String firstName;

    @Column(name = "last-name", nullable = false)
    private String lastName;

    @Column(name = "date-of-birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone-number", nullable = false)
    @Size(min = 10, max = 15, message = "Phone number should have at least 10 or less than 15 digits")
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
    private User user;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
