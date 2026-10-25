package com.example.employee_manager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // READ: Get all employees
    @GetMapping
public List<Employee> getAllEmployees(
        @RequestParam(required = false) String keyword) {

    if (keyword != null && !keyword.trim().isEmpty()) {
        return employeeService.searchEmployees(keyword.trim());
    }

    return employeeService.getAllEmployees();
}

    // READ: Get one employee
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);

        if (employee == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee not found");
        }

        return employee;
    }

    // CREATE: Add a new employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody Employee employee) {

        Employee savedEmployee = employeeService.saveEmployee(employee);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedEmployee);
    }

    // UPDATE: Update an existing employee
    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody Employee employee) {

        return employeeService.updateEmployee(id, employee);
    }

    // DELETE: Delete an employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}
