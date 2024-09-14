package com.michaelvol.bankingapp.holder.dto;

import com.michaelvol.bankingapp.holder.entity.Holder;

public record CreateHolderResponseDto(
        Holder holder,
        String message
) {
}
