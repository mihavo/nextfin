package com.michaelvol.bankingapp.holder.dto;

import com.michaelvol.bankingapp.holder.entity.Holder;
import com.michaelvol.bankingapp.users.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateCustomerResponseDto {
    @NotEmpty
    String message;

    @NotNull
    Holder holder;

    @NotNull
    User user;
}
