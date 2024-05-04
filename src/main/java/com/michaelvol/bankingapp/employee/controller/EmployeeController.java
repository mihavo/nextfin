package com.michaelvol.bankingapp.employee.controller;

import com.michaelvol.bankingapp.AppConstants;
import com.michaelvol.bankingapp.employee.dto.CreateEmployeeRequestDto;
import com.michaelvol.bankingapp.employee.dto.CreateEmployeeResponseDto;
import com.michaelvol.bankingapp.employee.entity.Employee;
import com.michaelvol.bankingapp.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

    private MessageSource messageSource;

    /**
     * Creates and stores an employee given a {@link CreateEmployeeResponseDto}
     * @param requestDto the request dto
     * @return the {@link CreateEmployeeResponseDto}
     */
    @PostMapping
    public ResponseEntity<CreateEmployeeResponseDto> createEmployee(@RequestBody CreateEmployeeRequestDto requestDto) {
        Employee employee = employeeService.createEmployee(requestDto);
        String successMessage = messageSource.getMessage("employee.create.success",
                                                         null,
                                                         LocaleContextHolder.getLocale());

        return new ResponseEntity<>(new CreateEmployeeResponseDto(employee.getId(), successMessage),
                                    HttpStatus.CREATED);
    }

    /**
     * Fetches an employee by its id
     * @return a {@link ResponseEntity} containing the Employee
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getHolderById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}
