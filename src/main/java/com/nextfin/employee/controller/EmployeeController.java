package com.nextfin.employee.controller;

import com.nextfin.AppConstants;
import com.nextfin.employee.dto.CreateEmployeeRequestDto;
import com.nextfin.employee.dto.CreateEmployeeResponseDto;
import com.nextfin.employee.dto.EmployeeMapper;
import com.nextfin.employee.dto.GetEmployeeDto;
import com.nextfin.employee.entity.Employee;
import com.nextfin.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstants.API_BASE_URL + "/employees/")
@Tag(name = "Employees API", description = "Methods that manage employee operations")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    private final MessageSource messageSource;
    private final EmployeeMapper employeeMapper;

    /**
     * Creates and stores an employee given a {@link CreateEmployeeResponseDto}
     * @param requestDto the request dto
     * @return the {@link CreateEmployeeResponseDto}
     */
    @PostMapping
    @Operation(summary = "Creates a bank employee")
    public ResponseEntity<CreateEmployeeResponseDto> createEmployee(@Valid @RequestBody CreateEmployeeRequestDto requestDto) {
        Employee employee = employeeService.createEmployee(requestDto);
        String message = messageSource.getMessage("employee.create.success", null, LocaleContextHolder.getLocale());
        CreateEmployeeResponseDto employeeResponse = employeeMapper.toCreateEmployeeResponseDto(employee, message);
        return new ResponseEntity<>(employeeResponse, HttpStatus.CREATED);
    }

    /**
     * Fetches an employee by its id
     * @return a {@link ResponseEntity} containing the Employee
     */
    @GetMapping("/{id}")
    @Operation(summary = "Fetches a bank employee by its ID")
    public ResponseEntity<GetEmployeeDto> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(new GetEmployeeDto(employee), HttpStatus.OK);
    }
}
