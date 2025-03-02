package com.nextfin.holder.dto;

import com.nextfin.holder.entity.Holder;

public record CreateHolderResponseDto(
        Holder holder,
        String message
) {
}
