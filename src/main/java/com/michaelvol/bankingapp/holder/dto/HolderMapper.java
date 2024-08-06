package com.michaelvol.bankingapp.holder.dto;

import com.michaelvol.bankingapp.holder.entity.Holder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HolderMapper {

    Holder createHolderDtoToHolder(CreateHolderDto dto);
}
