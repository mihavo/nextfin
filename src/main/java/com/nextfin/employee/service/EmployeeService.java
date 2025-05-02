package com.nextfin.employee.service;

import com.nextfin.employee.dto.CreateEmployeeRequestDto;
import com.nextfin.employee.entity.Employee;
import com.nextfin.employee.enums.EmployeeRole;
import jakarta.validation.Valid;

import java.util.List;
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

    /**
     * Fetches all employees from persistence
     *
     * @param role the role filter
     * @return the list of employees
     */
    List<Employee> getEmployees(@Valid EmployeeRole role);
}
