package com.reliaquest.api.service;

import com.reliaquest.api.mapper.EmployeeMapper;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.server.model.MockEmployee;
import com.reliaquest.server.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final RestTemplate restTemplate;

    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public EmployeeService() {
        this.restTemplate = new RestTemplate();
    }

    public List<Employee> getAllEmployees() {
        ResponseEntity<Response<List<MockEmployee>>> response = restTemplate.exchange(
                "http://localhost:8112/api/v1/employee",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody() != null && response.getBody().data() != null
                ? response.getBody().data().stream().map(EmployeeMapper::toEmployee).toList()
                : null;
    }

    public List<Employee> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = getAllEmployees();
        return employees != null
            ? employees.stream()
                .filter(e -> e != null && e.getName() != null && e.getName().toLowerCase().contains(searchString.toLowerCase()))
                .toList()
            : null;
    }

    public Employee getEmployeeById(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
        ResponseEntity<Response<MockEmployee>> response = restTemplate.exchange(
            "http://localhost:8112/api/v1/employee/" + uuid,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );
        return response.getBody() != null && response.getBody().data() != null
            ? EmployeeMapper.toEmployee(response.getBody().data())
            : null;
    }

    public Integer getHighestSalaryOfEmployees() {
        List<Employee> employees = getAllEmployees();
        return employees != null
            ? employees.stream()
                .map(Employee::getSalary)
                .max(Integer::compareTo)
                .orElse(0)
            : null;
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = getAllEmployees();
        return employees != null
            ? employees.stream()
                .sorted((e1, e2) -> e2.getSalary().compareTo(e1.getSalary()))
                .limit(10)
                .map(Employee::getName)
                .toList()
            : null;
    }

    public Employee createEmployee(EmployeeInput employeeInput) {
        ResponseEntity<Response<MockEmployee>> response = restTemplate.exchange(
            "http://localhost:8112/api/v1/employee",
            HttpMethod.POST,
            new org.springframework.http.HttpEntity<>(employeeInput),
            new ParameterizedTypeReference<>() {}
        );
        return response.getBody() != null && response.getBody().data() != null
            ? EmployeeMapper.toEmployee(response.getBody().data())
            : null;
    }

    public String deleteEmployeeById(String id) {
        Employee employee = getEmployeeById(id);
        if (employee == null) {
            return null;
        }
        ResponseEntity<Response<Boolean>> response = restTemplate.exchange(
            "http://localhost:8112/api/v1/employee",
            HttpMethod.DELETE,
            new org.springframework.http.HttpEntity<>(employee.getName()),
            new ParameterizedTypeReference<>() {}
        );
        return response.getBody() != null && response.getBody().data()
            ? employee.getName()
            : null;
    }
}
