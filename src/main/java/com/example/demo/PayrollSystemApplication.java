package com.example.demo;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.example.demo.model.Employee.EmployeeType.SALARIED;
import static com.example.demo.model.Employee.EmployeeType.HOURLY;

@SpringBootApplication
public class PayrollSystemApplication {
    @Autowired
    EmployeeService service;

    public static void main(String[] args) {
        SpringApplication.run(PayrollSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(EmployeeRepository repository) {
        return (args) -> {
            Scanner sc = new Scanner(System.in);
            List<String> salaryList = new ArrayList<>();

            service.createEmployee(new Employee("Oliver", 999.99, 0.0, 0, 0, SALARIED));
            service.createEmployee(new Employee("Tom", 1999.99, 0.0, 0, 0, HOURLY));
            service.createEmployee(new Employee("Tom", 1999.99, 0.0, 0, 0, HOURLY));
            service.createEmployee(new Employee("Tom", 1999.99, 0.0, 0, 0, HOURLY));

            while (true) {
                System.out.println("1. Calculate salary for Salaried Employee \n" +
                        "2. Calculate salary for Hourly Employee \n" +
                        "3. Calculate salary for Commission Employee\n" +
                        "4. Calculate salary for Salary plus Commission Employee\n" +
                        "5. Increase base salary for Salaried Employees\n" +
                        "6. Increase base salary for Salary plus Commission Employees");

                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        salaryList = service.getTotalSalaryByType(SALARIED);
                        output(salaryList);
                        break;
                    case 2:
                        salaryList = service.getTotalSalaryByType(Employee.EmployeeType.HOURLY);
                        output(salaryList);
                        break;
                    case 3:
                        salaryList = service.getTotalSalaryByType(Employee.EmployeeType.COMMISION);
                        output(salaryList);
                        break;
                    case 4:
                        salaryList = service.getTotalSalaryByType(Employee.EmployeeType.SALARIED_COMMISION);
                        output(salaryList);
                        break;
                    case 5:
                        System.out.println("How much percentage do you want to increase ?");
                        int percent = sc.nextInt();
                        service.increaseFixedSalaryByType(SALARIED, percent);
                        break;
                    case 6:
                        System.out.println("How much percentage do you want to increase ?");
                        percent = sc.nextInt();
                        service.increaseFixedSalaryByType(Employee.EmployeeType.SALARIED_COMMISION, percent);
                        break;
                }
            }
        };
    }

    private void output(List<String> salaryList) {
        for (String s : salaryList)
            System.out.println(s);
    }
}
