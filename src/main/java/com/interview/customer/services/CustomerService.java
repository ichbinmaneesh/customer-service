package com.interview.customer.services;

import com.interview.customer.dto.CustomerDTO;
import com.interview.customer.dto.CustomerRequestDTO;
import com.interview.customer.exceptions.CustomerNotFoundException;
import com.interview.customer.models.Customer;
import com.interview.customer.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepo;


    public List<CustomerDTO> getCustomers(){
        log.info("Fetching all customers");
        List<Customer> customers = customerRepo.findAll();
        return customers.stream().map(Customer::toDTO).toList();
    }

    public CustomerDTO saveCustomer(CustomerRequestDTO customerDTO) {
        log.info("Preparing to save customer with name {}", customerDTO.getFirstName());
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setDob(LocalDate.parse(customerDTO.getDob()));
         return customerRepo.save(customer).toDTO();
    }

    public CustomerDTO getCustomer(UUID id) {
        log.info("Fetching customer with id {}", id);
        Customer customer = customerRepo.findById(id).orElseThrow(()->new CustomerNotFoundException("user not found"));
        return customer.toDTO();
    }

    public CustomerDTO updateCustomer(UUID id, CustomerRequestDTO customerDTO) {
        log.info("Preparing to update customer with id {}", id);
        Customer customer = customerRepo.findById(id).orElseThrow(()->new CustomerNotFoundException("user not found"));
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setDob(LocalDate.parse(customerDTO.getDob()));
        return customerRepo.save(customer).toDTO();

    }

    public void deleteCustomer(UUID id) {
        log.info("Deleting customer with id {}", id);
        Customer customer = customerRepo.findById(id).orElseThrow(()->new CustomerNotFoundException("user not found"));
        customerRepo.delete(customer);
    }
}
