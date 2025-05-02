package com.nextfin.employee.service.impl;

import com.nextfin.common.address.entity.Address;
import com.nextfin.common.address.service.def.AddressService;
import com.nextfin.employee.dto.CreateEmployeeRequestDto;
import com.nextfin.employee.dto.EmployeeMapper;
import com.nextfin.employee.entity.Employee;
import com.nextfin.employee.enums.EmployeeRole;
import com.nextfin.employee.repository.EmployeeRepository;
import com.nextfin.employee.service.EmployeeService;
import com.nextfin.exceptions.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final AddressService addressService;

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    private final MessageSource messageSource;


    @Override
    public Employee createEmployee(CreateEmployeeRequestDto dto) {
        Address address = addressService.create(dto.getAddress());
        Employee employee = employeeMapper.toEmployee(dto);
        employee.setAddress(address);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long employeeId) throws NoSuchElementException {
        return employeeRepository.findById(employeeId)
                                 .orElseThrow(() -> new NotFoundException(messageSource.getMessage("employee.notfound",
                                                                                                   new Long[]{employeeId},
                                                                                                   LocaleContextHolder.getLocale())));
    }

    @Override
    public List<Employee> getEmployees(EmployeeRole role) {
        if (role == null) {
            return employeeRepository.findAll();
        }
        return employeeRepository.findAllByRole(role);
    }
}
