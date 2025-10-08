package com.reliaquest.api.service;

import com.reliaquest.api.mapper.EmployeeMapper;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeInput;
import com.reliaquest.server.model.MockEmployee;
import com.reliaquest.server.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    private MockEmployee mockEmployee;
    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockEmployee = MockEmployee.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .salary(100000)
                .age(30)
                .title("Engineer")
                .email("john.doe@example.com")
                .build();
        employee = EmployeeMapper.toEmployee(mockEmployee);
    }

    @Test
    void testGetAllEmployees_success() {
        Response<List<MockEmployee>> response = new Response<>(List.of(mockEmployee), Response.Status.HANDLED, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(response));
        List<Employee> result = employeeService.getAllEmployees();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetAllEmployees_nullResponse() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(null));
        List<Employee> result = employeeService.getAllEmployees();
        assertNull(result);
    }

    @Test
    void testGetEmployeesByNameSearch_found() {
        Response<List<MockEmployee>> response = new Response<>(List.of(mockEmployee), Response.Status.HANDLED, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(response));
        List<Employee> result = employeeService.getEmployeesByNameSearch("john");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetEmployeesByNameSearch_notFound() {
        Response<List<MockEmployee>> response = new Response<>(List.of(mockEmployee), Response.Status.HANDLED, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(response));
        List<Employee> result = employeeService.getEmployeesByNameSearch("jane");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetEmployeeById_success() {
        Response<MockEmployee> response = new Response<>(mockEmployee, Response.Status.HANDLED, null);
        when(restTemplate.exchange(contains("/" + mockEmployee.getId()), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(response));
        Employee result = employeeService.getEmployeeById(mockEmployee.getId().toString());
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetEmployeeById_notFound() {
        Response<MockEmployee> response = new Response<>(null, Response.Status.HANDLED, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(response));
        Employee result = employeeService.getEmployeeById("invalid-id");
        assertNull(result);
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        Response<List<MockEmployee>> response = new Response<>(List.of(mockEmployee), Response.Status.HANDLED, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(response));
        Integer result = employeeService.getHighestSalaryOfEmployees();
        assertNotNull(result);
        assertEquals(100000, result);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        Response<List<MockEmployee>> response = new Response<>(List.of(mockEmployee), Response.Status.HANDLED, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(response));
        List<String> result = employeeService.getTopTenHighestEarningEmployeeNames();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0));
    }

    @Test
    void testCreateEmployee_success() {
        Response<MockEmployee> response = new Response<>(mockEmployee, Response.Status.HANDLED, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(response));
        EmployeeInput input = new EmployeeInput();
        input.setName("John Doe");
        input.setSalary(100000);
        input.setAge(30);
        input.setTitle("Engineer");
        Employee result = employeeService.createEmployee(input);
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testDeleteEmployeeById_success() {
        Response<MockEmployee> getResponse = new Response<>(mockEmployee, Response.Status.HANDLED, null);
        Response<Boolean> deleteResponse = new Response<>(true, Response.Status.HANDLED, null);
        when(restTemplate.exchange(contains("/" + mockEmployee.getId()), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(getResponse));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(deleteResponse));
        String result = employeeService.deleteEmployeeById(mockEmployee.getId().toString());
        assertNotNull(result);
        assertEquals("John Doe", result);
    }

    @Test
    void testDeleteEmployeeById_notFound() {
        Response<MockEmployee> getResponse = new Response<>(null, Response.Status.HANDLED, null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(getResponse));
        String result = employeeService.deleteEmployeeById("invalid-id");
        assertNull(result);
    }
}

