package com.employeeservice.controller;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.employeeservice.exception.BusinessException;
import com.employeeservice.exception.EmployeeNotFoundException;
import com.employeeservice.model.Employee;
import com.employeeservice.service.EmployeeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Employee", description = "Operations on employees")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;
  
    @Operation(summary = "Get an employee by ID", description = "Provide an employee ID to look up detailed information")
    @GetMapping("/{empId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String empId) {
        log.info("API: Request to get employee by ID: {}", empId);
        Employee employee = employeeService.getEmployeeById(empId);
        log.debug("API: Employee found: {}", empId);
        return ResponseEntity.ok(employee);
    }

    @Operation(summary = "Getting all employees")
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("API: Request to get all employees.");
        List<Employee> employees = employeeService.getAllEmployees();
        log.debug("API: Returning all {} employees.", employees.size());
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Create a new employee")
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) {
        log.info("API: Request to add new employee: {}", employee.getEmpId());
        Employee newEmployee = employeeService.createEmployee(employee);
        if (newEmployee != null && newEmployee.getEmpId() != null) {
            log.info("API: Employee added successfully with ID: {}", newEmployee.getEmpId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
        } else {
            log.warn("API: Failed to add employee. Input: {}", employee.getEmpId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
    @Operation(summary = "Updating Existing employee")
    @PutMapping("/{empId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String empId,@Valid @RequestBody Employee employee) {
        log.info("API: Request to update employee with ID: {}", empId);
        employee.setEmpId(empId);
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        if (updatedEmployee != null) {
            log.info("API: Employee updated successfully: {}", empId);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            log.warn("API: Failed to update employee (other than not found): {}", empId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    @Operation(summary = "Delete employee with Id")
    @DeleteMapping("/{empId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String empId) {
        log.info("API: Request to delete employee with ID: {}", empId);
        employeeService.deleteEmployee(empId);
        log.info("API: Employee deletion processed for ID: {}", empId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Employee with ID " + empId + " deleted successfully.");
    }
    
    @Operation(summary = "Viewing details with Pagination")
 // Basic pagination
    @GetMapping("/page")
    public ResponseEntity<Map<String,Object>> getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "empId") String sortBy,
            @RequestParam(defaultValue = "ASC") String dir) {
        log.info("API: Request to Sorting and Pagination");
        Page<Employee> pg = employeeService.getEmployeesPage(page, size, sortBy, dir);

        Map<String,Object> response = new HashMap<>();
        response.put("employees", pg.getContent());
        response.put("page",      pg.getNumber());
        response.put("size",      pg.getSize());
        response.put("total",     pg.getTotalElements());
        response.put("pages",     pg.getTotalPages());
        response.put("last",      pg.isLast());
        log.info("API: Sorting Details Sucessful");
        return ResponseEntity.ok(response);

    }   
    
//    @GetMapping("/filter")
//    public ResponseEntity<Page<Employee>> filterByPosition(
//        @RequestParam String position,
//        @RequestParam(defaultValue="0") int page,
//        @RequestParam(defaultValue="10") int size,
//        @RequestParam(defaultValue="empId") String sortBy,
//        @RequestParam(defaultValue="ASC") String dir) {
//        log.info("API: Request to Filter by  Position");
//        Page<Employee> result = employeeService.getByPosition(position, page, size, sortBy, dir);
//        log.info("API: Filter by Position Sucessful");
//        return ResponseEntity.ok(result);
//    }
      
    
    @Operation(summary = "Filtering details with Position")
    @GetMapping("/filter")
    public ResponseEntity<Map<String,Object>> filterByPosition(
            @RequestParam String position,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "empId") String sortBy,
            @RequestParam(defaultValue = "ASC") String dir) {

        log.info("API: Request to Filter by Position = {}", position);
        Page<Employee> pg = employeeService.getByPosition(position, page, size, sortBy, dir);

        Map<String,Object> response = new HashMap<>();
        response.put("employees", pg.getContent());
        response.put("page",      pg.getNumber());
        response.put("size",      pg.getSize());
        response.put("total",     pg.getTotalElements());
        response.put("pages",     pg.getTotalPages());
        response.put("last",      pg.isLast());
        log.info("API: Filter by Position Successful, returned {} elements", pg.getNumberOfElements());

        return ResponseEntity.ok(response);
        
    }
     // --- DEDICATED LOGIN ENDPOINT ---
        // This new method handles login requests specifically
        // It is mapped to POST /api/employees/login (or whatever you define)
        @Operation(summary = "Employee Login", description = "Authenticates an employee using ID and Phone Number.")
        @PostMapping("/login") // This creates a new path: /api/employees/login
        public ResponseEntity<Map<String, Object>> employeeLogin(@RequestBody Map<String, String> credentials) {
            String empId = credentials.get("emp_id");
            String phone = credentials.get("phone");

            log.info("API: Login request for Employee ID: {}", empId);

            // Basic validation for presence of credentials
            if (empId == null || phone == null || empId.trim().isEmpty() || phone.trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Employee ID and Phone Number are required for login.");
                log.warn("API: Login failed - Missing credentials for Emp ID: {}", empId);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Call your service to handle the authentication logic
            // This method in EmployeeService should call employeeRepository.findByEmpIdAndPhone
            Optional<Employee> foundEmployee = employeeService.authenticateEmployee(empId, phone);

            Map<String, Object> response = new HashMap<>();
            if (foundEmployee.isPresent()) {
                Employee employee = foundEmployee.get();
                response.put("success", true);
                response.put("message", "Login successful!");
                // Return only necessary details for the frontend state
                Map<String, String> employeeDetails = new HashMap<>();
                employeeDetails.put("emp_id", employee.getEmpId());
                employeeDetails.put("emp_name", employee.getEmpName());
                employeeDetails.put("position", employee.getPosition());
                response.put("employee", employeeDetails);
                log.info("API: Login successful for Employee ID: {}", empId);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "Invalid Employee ID or Phone Number.");
                log.warn("API: Login failed - Invalid credentials for Emp ID: {}", empId);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
   
        }
        
}