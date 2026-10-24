package com.example.employee_manager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Read all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Search employees by name
    public List<Employee> searchEmployees(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllEmployees();
        }

        return employeeRepository
                .findByFullNameContainingIgnoreCase(keyword.trim());
    }

    // Read one employee
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Employee not found with ID: " + id
                    ));
    }

    // Create an employee
    public Employee saveEmployee(Employee employee) {
        employee.setId(null);
        return employeeRepository.save(employee);
    }

    // Update an employee
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existingEmployee = getEmployeeById(id);

        existingEmployee.setFullName(updatedEmployee.getFullName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setPhone(updatedEmployee.getPhone());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setSalary(updatedEmployee.getSalary());

        return employeeRepository.save(existingEmployee);
    }

    // Delete an employee
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    // Check whether an email already exists
    public boolean emailExists(String email) {
        return employeeRepository
                .findByEmailIgnoreCase(email).isPresent();
    }

    // Check whether an email belongs to a different employee
    public boolean emailExistsForAnotherEmployee(
            String email, Long employeeId) {

        return employeeRepository.findByEmailIgnoreCase(email)
                .map(employee ->
                    !employee.getId().equals(employeeId))
                .orElse(false);
    }
}
