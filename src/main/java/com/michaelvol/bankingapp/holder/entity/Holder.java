package com.michaelvol.bankingapp.holder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.michaelvol.bankingapp.account.entity.Account;
import com.michaelvol.bankingapp.common.address.entity.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "social-security-number")
    private String socialSecurityNumber;

    @Column(name = "username")
    @Size(min = 7, max = 28, message = "Username must be between 7 and 28 characters")
    private String username;

    @Column(name = "password")
    @Size(min = 7, max = 20, message = "Password must be between 7 and 20 characters")
    @JsonIgnore
    private String password;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @JsonIgnore
    private Address address;

    @ToString.Exclude
    @OneToMany(mappedBy = "holder")
    @JsonIgnore
    private List<Account> accounts;


    public String getFullName() {
        return firstName + " " + lastName;
    }
}
