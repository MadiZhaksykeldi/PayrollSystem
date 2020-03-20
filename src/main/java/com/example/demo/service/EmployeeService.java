package com.example.demo.service;

import com.example.demo.event.SalaryEvent;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.model.Employee.EmployeeType.SALARIED;
import static com.example.demo.model.Employee.EmployeeType.SALARIED_COMMISION;

@Service
public class EmployeeService implements ApplicationEventPublisherAware {

    @Autowired
    private final EmployeeRepository employeeRepository;
    private ApplicationEventPublisher eventPublisher;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public void createEmployee(Employee e) {
        employeeRepository.save(e);
    }

    public List<Employee> findAllEmplType(Employee.EmployeeType type) {
        return employeeRepository.findAllByEmplType(type);
    }

    public List<String> getTotalSalaryByType(Employee.EmployeeType type) {

        List<String> list = new ArrayList<>();

        for (Employee e : employeeRepository.findAllByEmplType(type)) {
            switch (type) {
                case SALARIED:
                    list.add(salaryString(e) + e.getFixedSalary());
                    break;
                case HOURLY:
                    list.add(salaryString(e) + calcHourly(e));
                    break;
                case COMMISION:
                    list.add(salaryString(e) + calcCommission(e));
                    break;
                case SALARIED_COMMISION:
                    list.add(salaryString(e) + calcSalaryPlus(e));
                    break;
            }
        }
        return list;
    }

    public void increaseFixedSalaryByType(Employee.EmployeeType type, int percent) {
        if (type.equals(SALARIED) || type.equals(SALARIED_COMMISION)) {
            for (Employee e : employeeRepository.findAllByEmplType(type)) {
                Double salary = e.getFixedSalary() * percent / 100 + e.getFixedSalary();
                e.setFixedSalary(salary);
                employeeRepository.save(e);
                this.eventPublisher.publishEvent(new SalaryEvent(this, e));
            }
        }
    }

    private String salaryString(Employee e) {
        return "id: " + e.getId() + ", name: " + e.getName() + ", type: " + e.getEmplType() + ", salary: ";
    }

    private Double calcHourly(Employee e) {
        Double salary = 0.0;
        if (e.getHoursWorked() > 40) {
            for (int i = 0; i < e.getHoursWorked() - 40; i++) {
                salary += 1.5 * e.getHourRate();
            }
            salary += e.getHourRate() * 40;
        } else {
            salary = e.getHourRate() * e.getHoursWorked();
        }
        return salary;
    }

    private Double calcCommission(Employee e) {
        Double salary = 0.0;
        for (int i = 0; i < e.getSales(); i++) {
            salary += e.getCommRate() * 20000;
        }
        return salary;
    }

    private Double calcSalaryPlus(Employee e) {
        Double salary = e.getFixedSalary();
        for (int i = 0; i < e.getSales(); i++) {
            salary += e.getCommRate() * 20000;
        }
        return salary;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
