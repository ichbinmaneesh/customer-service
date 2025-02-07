package com.interview.customer.models;

import com.interview.customer.dto.CustomerDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerId;

    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dob;

    public CustomerDTO toDTO(){
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(this.getCustomerId().toString());
        dto.setFirstName(this.getFirstName());
        dto.setLastName(this.getLastName());
        dto.setDateOfBirth(this.getDob());
        return dto;
    }
}
