package com.employeeservice.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.employeeservice.model.Employee;
import com.employeeservice.repo.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeDAO implements EmployeeDAOInterface {

    private static final Logger log = LoggerFactory.getLogger(EmployeeDAO.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee save(Employee employee) {
        log.debug("DAO: Saving employee: {}", employee.getEmpId());
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        log.debug("DAO: Updating employee: {}", employee.getEmpId());
        return employeeRepository.save(employee);
    }

    @Override
    public void delete(String empId) {
        log.debug("DAO: Deleting employee: {}", empId);
        employeeRepository.deleteById(empId);
    }

    @Override
    public Employee findById(String empId) {
        log.debug("DAO: Finding employee by ID: {}", empId);
        return employeeRepository.findById(empId).orElse(null);
    }

    @Override
    public List<Employee> findAll() {
        log.debug("DAO: Fetching all employees.");
        return employeeRepository.findAll();
    }

    @Override
    public boolean existsById(String empId) {
        log.debug("DAO: Checking if employee exists: {}", empId);
        return employeeRepository.existsById(empId);
    }
    
    @Override
    public Page<Employee> findAll(Pageable pageable) {
        log.debug("DAO: Sorthing the requested Details");
        return employeeRepository.findAll(pageable);
    }

    @Override
    public Page<Employee> findByPosition(String position, Pageable pageable) {
        return employeeRepository.findByPosition(position, pageable);
    }

 // --- NEW AUTHENTICATION DAO METHOD ---
    @Override
    public Optional<Employee> findByEmpIdAndPhone(String empId, String phone) {
        log.debug("DAO: Authenticating employee with ID: {} and phone: {}", empId, phone);
        return employeeRepository.findByEmpIdAndPhone(empId, phone); // DAO calls Repository
    }
}