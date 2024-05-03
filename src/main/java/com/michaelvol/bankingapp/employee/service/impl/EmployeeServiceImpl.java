package com.michaelvol.bankingapp.employee.service.impl;

import com.michaelvol.bankingapp.employee.dto.CreateEmployeeRequestDto;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.employee.repository.EmployeeRepository;
import com.michaelvol.bankingapp.employee.service.EmployeeService;
import com.michaelvol.bankingapp.holder.entity.Holder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    EmployeeRepository employeeRepository;

    @Override
    public Holder createEmployee(CreateEmployeeRequestDto dto) {
        return null;
    }

    @Override
    public Employee getEmployeeById(Long employeeId) throws NoSuchElementException {
        return employeeRepository.findById(employeeId).orElseThrow(NoSuchElementException::new);
    }
}
