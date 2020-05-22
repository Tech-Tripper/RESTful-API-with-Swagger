package com.codeflix.swagger.controller;


import com.codeflix.swagger.exception.ResourceNotFoundException;
import com.codeflix.swagger.model.Employee;
import com.codeflix.swagger.repository.EmployeeRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(value = "Employee Roll-Call System", description = "Operations pertaining to employee in Employee Roll-Call System")
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {


    @Autowired
    private EmployeeRepository employeeRepository;


    @ApiOperation(value = "Displays a List of available employees", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
            })
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }


    @ApiOperation(value="Get an employee by Id")
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeesById(@ApiParam(value = "Employee Id from which single employee object will retrieve", required = true) @PathVariable(value = "id") Long employeeId) throws ResourceNotFoundException{

        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

            return ResponseEntity.ok().body(employee);
    }


    @ApiOperation(value ="Add an employee")
    @PostMapping("/employees")
    public Employee createEmployee(@ApiParam(value = "Employee object stored in database table ", required = true)@Valid @RequestBody Employee employee){

        return employeeRepository.save(employee);
    }

    @ApiOperation(value ="Update an employee")
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@ApiParam(value = "Employee Id to update employee object", required = true) @PathVariable(value = "id") Long employeeId,
                                                   @ApiParam(value = "Update employee object", required = true) @Valid @RequestBody Employee employeeDetails) throws  ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @ApiOperation(value = "Delete an employee")
    @DeleteMapping("/employees/{id}")
    public Map<String,Boolean> deleteEmployee(@ApiParam(value = "Employee Id from which an employee object will be deleted from database table") @PathVariable(value="id") Long employeeId) throws ResourceNotFoundException{

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return  response;
    }

}
