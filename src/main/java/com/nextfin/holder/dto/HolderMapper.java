package com.nextfin.holder.dto;

import com.nextfin.holder.entity.Holder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HolderMapper {

    Holder createHolderDtoToHolder(CreateHolderDto dto);
}
