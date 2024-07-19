package com.michaelvol.bankingapp.holder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.michaelvol.bankingapp.common.address.dto.AddressDataDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateHolderRequestDto {
    @NotEmpty(message = "First name cannot be empty")
    public String firstName;

    @NotEmpty(message = "Last Name cannot be empty")
    public String lastName;

    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "dd/MM/yyyy:HH:mm:ss")
    public LocalDate dateOfBirth;

    @Size(min = 10, max = 15, message = "Phone number should be between 10 and 15 digits")
    public String phoneNumber;

    @Email(message = "Email must have the form user@example.com")
    public String email;

    @Size(min = 9, max = 9, message = "Social Security Number must be 9 digits")
    public String socialSecurityNumber;

    @Size(min = 7, max = 28, message = "Username must be between 7 and 28 characters")
    public String username;

    @Size(min = 7, max = 20, message = "Password must be between 7 and 20 characters")
    public String password;

    @Valid
    @NotNull(message = "The holder must provide a valid address")
    AddressDataDto address;
}
