package com.alby.exam;

import com.alby.exam.model.Employee;
import com.alby.exam.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public String home(Model model){
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "add_employee";
    }
    @GetMapping("/employees")
    public String listOfEmployees(Model model){
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees";
    }
    @GetMapping("/employee/edit/{id}")
    public String getEmployeeById(@PathVariable Long id, Model model){
        model.addAttribute("employee", employeeService.getEmployeeById(id));
        return "edit_employee";
    }
    @PostMapping("/add/employee")
    public String saveEmployee(Employee employee, RedirectAttributes redirectAttributes){
        String message = null;
        if(employee.getBirthDate()!=null){
            LocalDate localBirthDate = employee.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            double age = (double) Period.between(localBirthDate, currentDate).getYears();
            employee.setAge(age);
            message = "Employee added successfully and age calculated";
        }
       else{
            employee.setAge(0.0);
        }
       employeeService.addEmployee(employee);
       redirectAttributes.addFlashAttribute("message", message);
       return "redirect:/";
    }
    @PostMapping("/employee/update/{id}")
    public String updateEmployee(@PathVariable Long id, Employee employee, RedirectAttributes redirectAttributes){
        Employee existEmployee = employeeService.getEmployeeById(id);
        existEmployee.setName(employee.getName());
        existEmployee.setBirthDate(employee.getBirthDate());
        existEmployee.setSalary(employee.getSalary());
        existEmployee.setGrade(employee.getGrade());
        if(existEmployee.getBirthDate()!=null){
            LocalDate localBirthDate = existEmployee.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();
            double age = (double) Period.between(localBirthDate, currentDate).getYears();
            existEmployee.setAge(age);
        }
        else{
            existEmployee.setAge(0.0);
        }
        employeeService.addEmployee(existEmployee);
        return "redirect:/employees";
    }
    @GetMapping("/employee/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes){
         employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("message", "Employee ID: "+ id +" Delete Successfully");
        return "redirect:/employees";
    }
}
