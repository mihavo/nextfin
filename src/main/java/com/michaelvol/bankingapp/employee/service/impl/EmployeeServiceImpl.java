package com.michaelvol.bankingapp.employee.service.impl;

import com.michaelvol.bankingapp.employee.dto.CreateEmployeeRequestDto;
import com.michaelvol.bankingapp.employee.dto.EmployeeMapper;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.employee.repository.EmployeeRepository;
import com.michaelvol.bankingapp.employee.service.EmployeeService;
import com.michaelvol.bankingapp.exceptions.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    private final MessageSource messageSource;

    @Override
    public Employee createEmployee(CreateEmployeeRequestDto dto) {
        Employee employee = employeeMapper.toEmployee(dto);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long employeeId) throws NoSuchElementException {
        return employeeRepository.findById(employeeId)
                                 .orElseThrow(() -> new NotFoundException(messageSource.getMessage("employee.notfound",
                                                                                                   new Long[]{employeeId},
                                                                                                   LocaleContextHolder.getLocale())));
    }
}
