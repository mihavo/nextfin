package com.nextfin.employee.dto;

import com.nextfin.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "address", ignore = true)
    Employee toEmployee(CreateEmployeeRequestDto requestDto);

    CreateEmployeeResponseDto toCreateEmployeeResponseDto(Employee employee, String message);
}
