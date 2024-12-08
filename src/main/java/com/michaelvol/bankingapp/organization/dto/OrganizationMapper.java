package com.michaelvol.bankingapp.organization.dto;

import com.michaelvol.bankingapp.organization.entity.Organization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    Organization toOrganization(CreateOrganizationDto dto);
}
