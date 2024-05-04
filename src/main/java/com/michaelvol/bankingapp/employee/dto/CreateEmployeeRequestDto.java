package com.michaelvol.bankingapp.employee.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateEmployeeRequestDto {
    @NotEmpty(message = "First name cannot be empty")
    public String firstName;

    @NotEmpty(message = "Last Name cannot be empty")
    public String lastName;

    @Past(message = "date of birth must be in the past")
    public String dateOfBirth;

    @Size(min = 10, max = 15, message = "Phone number should be between 10 and 15 digits")
    public String phoneNumber;
}
