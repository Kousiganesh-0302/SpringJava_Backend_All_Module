package com.employeeservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.employeeservice.dao.EmployeeDAO;
import com.employeeservice.exception.BusinessException;
import com.employeeservice.exception.EmployeeNotFoundException;
import com.employeeservice.model.Employee;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements EmployeeServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeDAO employeeDAO;
    
    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        log.info("SERVICE: Attempting to add employee: {}", employee.getEmpId());
        
     // Convert input ID to proper format if needed
        if (employee.getEmpId() != null && !employee.getEmpId().startsWith("EMP")) {
            employee.setEmpId("EMP" + employee.getEmpId());
        }
        
        if (employeeDAO.existsById(employee.getEmpId())) {
            log.warn("SERVICE: Employee ID {} already exists.", employee.getEmpId());
            throw new BusinessException("Employee ID already exists");
        }
        Employee savedEmployee = employeeDAO.save(employee);
        log.info("SERVICE: Employee added successfully: {}", savedEmployee.getEmpId());
        return savedEmployee;
    }

    @Override
    @Transactional
    public Employee updateEmployee(Employee employee) {
        log.info("SERVICE: Updating employee: {}", employee.getEmpId());
        if (!employeeDAO.existsById(employee.getEmpId())) {
            log.warn("SERVICE: Employee not found: {}", employee.getEmpId());
            throw new EmployeeNotFoundException("Employee not found");
        }
        return employeeDAO.update(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(String empId) {
        log.info("SERVICE: Deleting employee: {}", empId);
        if (!employeeDAO.existsById(empId)) {
            log.warn("SERVICE: Employee not found: {}", empId);
            throw new EmployeeNotFoundException("Employee not found");
        }
        employeeDAO.delete(empId);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeById(String empId) {
        log.info("SERVICE: Fetching employee by ID: {}", empId);
        Employee employee = employeeDAO.findById(empId);
        if (employee == null) {
            log.warn("SERVICE: Employee not found: {}", empId);
            throw new EmployeeNotFoundException("Employee not found");
        }
        return employee;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        log.info("SERVICE: Fetching all employees.");
        return employeeDAO.findAll();
    }

    @Override
    public boolean employeeExists(String empId) {
        log.debug("SERVICE: Checking if employee exists: {}", empId);
        return employeeDAO.existsById(empId);
    }
    
    @Transactional(readOnly = true)
    public Page<Employee> getEmployeesPage(int page, int size, String sortBy, String direction) {
        log.info("SERVICE: Sorting with pagination");
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeDAO.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Employee> getByPosition(String pos, int page, int size, String sortBy, String dir) {
        Sort sort = Sort.by(Sort.Direction.fromString(dir), sortBy);
        Pageable pg = PageRequest.of(page, size, sort);
        return employeeDAO.findByPosition(pos, pg);
    }
 // --- NEW AUTHENTICATION LOGIC ---
    @Override // Add @Override since it's in EmployeeServiceInterface
    @Transactional(readOnly = true)
    public Optional<Employee> authenticateEmployee(String empId, String phone) {
        log.info("SERVICE: Authenticating employee with ID: {}", empId);
        // Service now calls DAO for authentication, and DAO calls Repository
        return employeeDAO.findByEmpIdAndPhone(empId, phone);
    }
}