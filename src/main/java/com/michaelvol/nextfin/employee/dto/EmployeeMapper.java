package com.michaelvol.nextfin.employee.dto;

import com.michaelvol.nextfin.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "address", ignore = true)
    Employee toEmployee(CreateEmployeeRequestDto requestDto);
}
