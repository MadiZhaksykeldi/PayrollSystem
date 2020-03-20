package com.example.demo.event;

import com.example.demo.model.Employee;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SalaryEventHandler implements ApplicationListener<SalaryEvent> {

    @Override
    public void onApplicationEvent(SalaryEvent salaryEvent) {
        Employee employee = salaryEvent.getEmployee();
        System.out.println("Salary was changed for: " + employee.getName());
    }
}
