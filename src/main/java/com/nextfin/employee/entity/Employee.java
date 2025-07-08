package com.nextfin.employee.entity;

import com.nextfin.common.address.entity.Address;
import com.nextfin.employee.enums.EmployeeRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class Employee {
    @Id
    @SequenceGenerator(name = "employee_gen", sequenceName = "employee_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_gen")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date-of-birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone-number", nullable = false)
    @Size(min = 10, max = 15, message = "Phone number should have at least 10 or less than 15 digits")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "employee_role", nullable = false)
    @Builder.Default
    private EmployeeRole role = EmployeeRole.OTHER;
}
