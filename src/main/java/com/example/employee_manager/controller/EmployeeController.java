
package com.example.employee_manager.controller;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // READ: Display employees and registration form
    @GetMapping
public String listEmployees(
        @RequestParam(required = false) String keyword,
        Model model) {

    List<Employee> employees;

    if (keyword != null && !keyword.trim().isEmpty()) {
        employees = employeeService.searchEmployees(keyword.trim());
    } else {
        employees = employeeService.getAllEmployees();
    }

    model.addAttribute("employees", employees);
    model.addAttribute("employee", new Employee());
    model.addAttribute("keyword", keyword);

    return "employees";
}

    // CREATE: Save a new employee
    @PostMapping({"/save", ""})
    public String createEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "employees";
        }

        try {
            employeeService.saveEmployee(employee);
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("errorMessage",
                    "This email is already registered.");
            return "employees";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Employee added successfully!");

        return "redirect:/employees";
    }

    // UPDATE: Load selected employee into the main employees page
    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("employee", employeeService.getEmployeeById(id));
        model.addAttribute("employees", employeeService.getAllEmployees());

        return "employees";
    }

    // UPDATE: Save modified employee details
    @PostMapping("/update/{id}")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Preserve the employee ID during validation
        employee.setId(id);

        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            return "employees";
        }

        try {
            employeeService.updateEmployee(id, employee);
        } catch (DataIntegrityViolationException ex) {
            model.addAttribute("employees", employeeService.getAllEmployees());
            model.addAttribute("errorMessage",
                    "This email is already registered to another employee.");
            return "employees";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Employee updated successfully!");

        return "redirect:/employees";
    }

    // DELETE: Remove an employee
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
