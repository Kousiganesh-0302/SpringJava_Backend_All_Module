package com.employeeservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "empformservlet")
public class Employee extends Auditable {
    
	@Id
    @Column(name = "emp_id", length = 255)	
    @Pattern(regexp = "^EMP\\d{1,7}$", 
             message = "ID must be 'EMP' followed by 1-7 digits")
    private String empId;

    @NotBlank(message = "Employee name is required")
    @Size(max = 20, message = "Name must be less than 20 characters")
    private String empName;

    @NotBlank(message = "Position is required")
    private String position;

    @Pattern(regexp = "^\\d{10}$", 
             message = "Phone must be 10 digits")
    private String phone;

    // Getters and Setters
    public String getEmpId() { return empId; }
    
    public void setEmpId(String empId) {
        // If input doesn't start with EMP, add it
        if (empId != null && !empId.startsWith("EMP")) {
            this.empId = "EMP" + empId;
        } else {
            this.empId = empId;
        }
    }    
    
    public String getEmpName() { return empName; }
    public void setEmpName(String empName) { this.empName = empName; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}