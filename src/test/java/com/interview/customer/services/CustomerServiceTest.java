package com.interview.customer.services;

import com.interview.customer.dto.CustomerDTO;
import com.interview.customer.dto.CustomerRequestDTO;
import com.interview.customer.exceptions.CustomerNotFoundException;
import com.interview.customer.models.Customer;
import com.interview.customer.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerRequestDTO customerRequestDTO;
    private CustomerRequestDTO updateCustomerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerRepository);

        customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setFirstName("Maneesh");
        customer.setLastName("Pandey");
        customer.setDob(LocalDate.of(1990, 1, 1));

        customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setFirstName("Maneesh");
        customerRequestDTO.setLastName("Pandey");
        customerRequestDTO.setDob("1990-01-01");

        updateCustomerDTO = new CustomerRequestDTO();
        updateCustomerDTO.setFirstName("Neeraj");
        updateCustomerDTO.setLastName("Pandey");
        updateCustomerDTO.setDob("1990-01-01");
    }

    @Test
    void shouldReturnAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        List<CustomerDTO> customers = customerService.getCustomers();
        assertEquals(1, customers.size());
        assertEquals("Maneesh", customers.getFirst().getFirstName());
    }

    @Test
    void shouldCreateNewCustomerGivenValidData() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerDTO savedCustomer = customerService.saveCustomer(customerRequestDTO);
        assertNotNull(savedCustomer);
        assertEquals("Maneesh", savedCustomer.getFirstName());
    }

    @Test
    void shouldReturnCustomerGivenValidCustomerId() {
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        CustomerDTO foundCustomer = customerService.getCustomer(customer.getCustomerId());
        assertNotNull(foundCustomer);
        assertEquals("Maneesh", foundCustomer.getFirstName());
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionGivenInvalidId() {
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomer(UUID.randomUUID()));
    }

    @Test
    void shouldUpdateCustomerGivenValidId() {
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerDTO updatedCustomer = customerService.updateCustomer(customer.getCustomerId(), updateCustomerDTO);
        assertNotNull(updatedCustomer);
        assertEquals("Neeraj", updatedCustomer.getFirstName());
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionGivenInvalidCustomerId() {
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () ->customerService
                .updateCustomer(customer.getCustomerId(), updateCustomerDTO));
    }

    @Test
    void shouldDeleteCustomerGivenValidId() {
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(any(Customer.class));
        customerService.deleteCustomer(customer.getCustomerId());
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionGivenWrongCustomerId() {
        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () ->customerService
                .deleteCustomer(customer.getCustomerId()));
    }
}
