package com.nextfin.holder.dto;

import com.nextfin.holder.entity.Holder;
import com.nextfin.users.entity.User;
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
