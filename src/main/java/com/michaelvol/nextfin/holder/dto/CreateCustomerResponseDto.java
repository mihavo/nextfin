package com.michaelvol.nextfin.holder.dto;

import com.michaelvol.nextfin.holder.entity.Holder;
import com.michaelvol.nextfin.users.entity.User;
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
