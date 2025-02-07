package com.interview.customer.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.interview.customer.controllers.CustomersController;
import com.interview.customer.dto.CustomerDTO;
import com.interview.customer.dto.CustomerRequestDTO;
import com.interview.customer.models.Customer;
import com.interview.customer.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class CustomersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;
    @InjectMocks
    private CustomersController customerDetailsController;

    private List<Customer> customers;
    private Customer customer;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        customerDetailsController = new CustomersController(customerService);
        customers = new ArrayList<>();
        mockMvc = MockMvcBuilders.standaloneSetup(customerDetailsController).build();

        customer = new Customer();
        customer.setCustomerId(UUID.randomUUID());
        customer.setFirstName("Maneesh");
        customer.setLastName("Pandey");
        customer.setDob(LocalDate.of(1990, 1, 1));
        customers.add(customer);
    }

    @Test
    public void shouldReturnAllCustomers() throws Exception {
        List<CustomerDTO> customerDTO = customers.stream().map(Customer::toDTO).toList();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonData = mapper.writeValueAsString(customerDTO);
        when(customerService.getCustomers()).thenReturn(customerDTO);
        mockMvc.perform(get("/customers")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(jsonData));
    }

    @Test
    public void shouldReturnSpecificCustomer() throws Exception {
        CustomerDTO customerDTO = customer.toDTO();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonData = mapper.writeValueAsString(customerDTO);
        when(customerService.getCustomer(any(UUID.class))).thenReturn(customerDTO);
        mockMvc.perform(get("/customers/" + customer.getCustomerId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(jsonData));
    }

    @Test
    public void shouldCreateCustomer() throws Exception {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setFirstName("Maneesh");
        customerRequestDTO.setLastName("Pandey");
        customerRequestDTO.setDob("1990-01-01");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonRequest = mapper.writeValueAsString(customerRequestDTO);
        String jsonResponse = mapper.writeValueAsString(customer.toDTO());

        when(customerService.saveCustomer(Mockito.any(CustomerRequestDTO.class))).thenReturn(customer.toDTO());

        mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void shouldReturnBadRequestForInvalidCustomer() throws Exception {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        // Intentionally leaving out required fields to simulate a bad request

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonRequest = mapper.writeValueAsString(customerRequestDTO);

        mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestForInvalidCustomerId() throws Exception {
        String customerId = "invalid-customer-id";
        mockMvc.perform(get("/customers/" + customerId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnOkForValidCustomerUpdate() throws Exception {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setFirstName("Neeraj");
        customerRequestDTO.setLastName("Pandey");
        customerRequestDTO.setDob("1990-01-01");

        customer.setFirstName("Neeraj");
        customer.setLastName("Pandey");
        customer.setDob(LocalDate.of(1990, 1, 1));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonRequest = mapper.writeValueAsString(customerRequestDTO);
        String jsonResponse = mapper.writeValueAsString(customer.toDTO());

        when(customerService.updateCustomer(Mockito.any(UUID.class), Mockito.any(CustomerRequestDTO.class))).thenReturn(customer.toDTO());

        mockMvc.perform(put("/customers/" + customer.getCustomerId())
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @Test
    public void shouldReturnBadRequestForInvalidCustomerUpdate() throws Exception {
        CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
        // Intentionally leaving out required fields to simulate a bad request

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonRequest = mapper.writeValueAsString(customerRequestDTO);

        mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnNoContentForValidCustomerDelete() throws Exception {
        String customerId = UUID.randomUUID().toString();
        doNothing().when(customerService).deleteCustomer(Mockito.any(UUID.class));
        mockMvc.perform(delete("/customers/" + customerId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void shouldReturnBadRequestForInvalidCustomerDelete() throws Exception {
        String customerId = "invalid-customer-id";
        mockMvc.perform(delete("/customers/" + customerId))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
