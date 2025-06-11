package com.employeeservice.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.employeeservice.model.Employee;

public interface EmployeeDAOInterface {
    Employee save(Employee employee);
    Employee update(Employee employee);
    void delete(String empId);
    Employee findById(String empId);
    List<Employee> findAll();
    boolean existsById(String empId);
    
 // Add pagination methods
    Page<Employee> findAll(Pageable pageable); 
    Page<Employee> findByPosition(String position, Pageable pageable);
    Optional<Employee> findByEmpIdAndPhone(String empId, String phone);

}