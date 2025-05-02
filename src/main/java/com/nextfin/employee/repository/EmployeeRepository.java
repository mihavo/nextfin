package com.nextfin.employee.repository;

import com.nextfin.employee.entity.Employee;
import com.nextfin.employee.enums.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByRole(EmployeeRole role);
}
