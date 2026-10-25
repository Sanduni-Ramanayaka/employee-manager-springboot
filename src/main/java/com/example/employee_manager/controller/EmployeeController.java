package com.example.employee_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Read: Display all employees and the registration form
    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute(
                "employees", employeeService.getAllEmployees());
        model.addAttribute("employee", new Employee());

        return "employees";
    }

    // Create: Save a new employee
    @PostMapping
    public String createEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute(
                    "employees", employeeService.getAllEmployees());

            return "employees";
        }

        employeeService.saveEmployee(employee);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Employee added successfully!");

        return "redirect:/employees";
    }

    // Update: Display the employee edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "employee", employeeService.getEmployeeById(id));

        return "edit-employee";
    }

    // Update: Save the modified employee details
    @PostMapping("/update/{id}")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            employee.setId(id);
            return "edit-employee";
        }

        employeeService.updateEmployee(id, employee);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Employee updated successfully!");

        return "redirect:/employees";
    }

    // Delete: Remove an employee
    @PostMapping("/delete/{id}")
    public String deleteEmployee(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        employeeService.deleteEmployee(id);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Employee deleted successfully!");

        return "redirect:/employees";
    }
}
