package com.nextfin.employee.dto;

import com.nextfin.common.address.entity.Address;

import java.time.LocalDate;

public record CreateEmployeeResponseDto(Long id, String firstName, String lastName, LocalDate dateOfBirth,
                                        String phoneNumber, Address address, String message) {
}
