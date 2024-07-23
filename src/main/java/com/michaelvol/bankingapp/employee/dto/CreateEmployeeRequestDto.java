package com.michaelvol.bankingapp.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.michaelvol.bankingapp.common.address.dto.AddressDataDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateEmployeeRequestDto {
    @NotEmpty(message = "First name cannot be empty")
    public String firstName;

    @NotEmpty(message = "Last Name cannot be empty")
    public String lastName;

    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "dd/MM/yyyy:HH:mm:ss")
    public LocalDate dateOfBirth;

    @Size(min = 10, max = 15, message = "Phone number should be between 10 and 15 digits")
    public String phoneNumber;

    @Valid
    @NotNull(message = "Employee must have a valid address")
    public AddressDataDto address;
}
