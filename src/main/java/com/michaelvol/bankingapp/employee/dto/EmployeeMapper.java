package com.michaelvol.bankingapp.employee.dto;

import com.michaelvol.bankingapp.employee.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee toEmployee(CreateEmployeeRequestDto requestDto);
}
