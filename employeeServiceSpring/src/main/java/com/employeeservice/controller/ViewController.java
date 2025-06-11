package com.employeeservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping({"/", "/employees"})
    public String showEmployeeListPage() {
        return "employee/list"; // employee/list.html
    }

    @GetMapping("/employees/add")
    public String showAddEmployeePage() {
        return "employee/form"; // employee/form.html
    }

}