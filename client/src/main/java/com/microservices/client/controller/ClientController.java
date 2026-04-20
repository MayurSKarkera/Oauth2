package com.microservices.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.microservices.client.model.Employee;
import com.microservices.client.service.EmployeeService;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private EmployeeService service;

    @GetMapping("/get-employee")
    public Employee getEmployee() {
        return service.callServer();
    }
}