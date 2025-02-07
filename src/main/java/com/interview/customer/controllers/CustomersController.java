package com.interview.customer.controllers;


import com.interview.customer.dto.CustomerDTO;
import com.interview.customer.dto.CustomerRequestDTO;
import com.interview.customer.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomersController {

    private final CustomerService customerService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@RequestBody  @Valid CustomerRequestDTO customer){
        log.info("Creating customer with name {}", customer.getFirstName());
        return customerService.saveCustomer(customer);
    }


    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO getCustomer(@PathVariable UUID id){
        return customerService.getCustomer(id);
    }

    @PutMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO updateCustomer(@PathVariable UUID customerId, @RequestBody @Valid CustomerRequestDTO customer){
        return customerService.updateCustomer(customerId, customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable UUID id){
        customerService.deleteCustomer(id);
    }
}
