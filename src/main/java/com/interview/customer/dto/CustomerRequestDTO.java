package com.interview.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerRequestDTO {

    @NotBlank(message = "First name is mandatory")
    @Pattern(regexp = "^[a-zA-Z]{1,255}$", message = "First name should contain only alphabets")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Pattern(regexp = "^[a-zA-Z]{1,255}$", message = "Last name should contain only alphabets")
    private String lastName;

    @NotNull(message = "Date of birth is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Date of birth should be in yyyy-MM-dd format")
    private String dob;
}
