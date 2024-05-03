package com.michaelvol.bankingapp.employee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@AllArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class Employee {
    @Id
    @SequenceGenerator(name = "employee_gen", sequenceName = "employee_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_gen")
    private Long id;


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date-of-birth", nullable = false)
    private LocalDate dateOfBirth;
}
