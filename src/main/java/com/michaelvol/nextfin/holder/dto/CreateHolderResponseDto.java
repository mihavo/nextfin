package com.michaelvol.nextfin.holder.dto;

import com.michaelvol.nextfin.holder.entity.Holder;

public record CreateHolderResponseDto(
        Holder holder,
        String message
) {
}
