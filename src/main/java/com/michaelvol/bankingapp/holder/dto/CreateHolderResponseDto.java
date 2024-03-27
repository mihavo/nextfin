package com.michaelvol.bankingapp.holder.dto;

import com.michaelvol.bankingapp.holder.entity.Holder;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateHolderResponseDto {
    @NotEmpty
    String message;

    @NotNull
    Holder holder;
}
