package com.michaelvol.nextfin.organization.dto;

import com.michaelvol.nextfin.organization.entity.Organization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    Organization toOrganization(CreateOrganizationDto dto);
}
