package com.reliaquest.api.mapper;

import com.reliaquest.api.model.Employee;
import com.reliaquest.server.model.MockEmployee;

public class EmployeeMapper {
    public static Employee toEmployee(MockEmployee mockEmployee) {
        if (mockEmployee == null) return null;
        Employee employee = new Employee();
        employee.setId(mockEmployee.getId());
        employee.setName(mockEmployee.getName());
        employee.setSalary(mockEmployee.getSalary());
        employee.setAge(mockEmployee.getAge());
        employee.setTitle(mockEmployee.getTitle());
        employee.setEmail(mockEmployee.getEmail());
        return employee;
    }
}
