package com.michaelvol.bankingapp.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter

public class CreateUserDto {

	@NotEmpty(message = "Username is required")
	@Size(min = 3, max = 28, message = "Username must be between 3 and 28 characters")
	private String username;

	@NotEmpty(message = "Password is required")
	@Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
	private String password;

	@NotEmpty(message = "Email is required")
	@Email(message = "Email must have the form user@example.com")
	public String email;

	@NotEmpty(message = "Phone Number is required")
	@Size(min = 10, max = 15, message = "Phone number should be between 10 and 15 digits")
	public String phoneNumber;

	@NotEmpty(message = "Social security number is required")
	@Size(min = 9, max = 9, message = "Social Security Number must be 9 digits")
	public String socialSecurityNumber;
}
