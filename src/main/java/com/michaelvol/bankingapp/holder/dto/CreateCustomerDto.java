package com.michaelvol.bankingapp.holder.dto;

import com.michaelvol.bankingapp.users.dto.CreateUserDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCustomerDto {
    @Valid
    @NotNull(message = "Holder data must be provided")
    private CreateHolderDto holder;

    @Valid
    @NotNull(message = "User data must be provided")
    private CreateUserDto user;
}
