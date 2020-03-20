package com.example.demo.event;

import com.example.demo.model.Employee;
import org.springframework.context.ApplicationEvent;

public class SalaryEvent extends ApplicationEvent {
    private Employee employee;

    public SalaryEvent(Object source, Employee employee) {
        super(source);
        this.employee = employee;
    }
    public Employee getEmployee(){
        return employee;
    }
}