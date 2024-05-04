package com.michaelvol.bankingapp.employee.service.impl;

import com.michaelvol.bankingapp.employee.dto.CreateEmployeeRequestDto;
import com.michaelvol.bankingapp.employee.dto.EmployeeMapper;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.employee.repository.EmployeeRepository;
import com.michaelvol.bankingapp.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepository;
    EmployeeMapper employeeMapper;

    @Override
    public Employee createEmployee(CreateEmployeeRequestDto dto) {
        Employee employee = employeeMapper.toEmployee(dto);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long employeeId) throws NoSuchElementException {
        return employeeRepository.findById(employeeId).orElseThrow(NoSuchElementException::new);
    }
}
