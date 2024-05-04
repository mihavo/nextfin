package com.michaelvol.bankingapp.employee.service;

import com.michaelvol.bankingapp.employee.dto.CreateEmployeeRequestDto;
import com.michaelvol.bankingapp.employee.entity.Employee;

import java.util.NoSuchElementException;

public interface EmployeeService {


    /**
     * Creates a new Employee entity and persists it to DB
     *
     * @param dto the {@link CreateEmployeeRequestDto}
     * @return the created employee
     */
    Employee createEmployee(CreateEmployeeRequestDto dto);

    /**
     * Fetches an employee from persistence from its id
     *
     * @param employeeId the holder's id
     * @return the holder
     */
    Employee getEmployeeById(Long employeeId) throws NoSuchElementException;
}
