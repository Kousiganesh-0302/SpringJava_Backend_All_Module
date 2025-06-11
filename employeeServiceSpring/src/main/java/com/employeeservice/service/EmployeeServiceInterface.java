package com.employeeservice.service;

import java.util.List;
import java.util.Optional;

import com.employeeservice.model.Employee;

public interface EmployeeServiceInterface {
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Employee employee);
    void deleteEmployee(String empId);
    Employee getEmployeeById(String empId);
    List<Employee> getAllEmployees();
    boolean employeeExists(String empId);
    Optional<Employee> authenticateEmployee(String empId, String phone);

    
}