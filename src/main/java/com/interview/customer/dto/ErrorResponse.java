package com.interview.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ErrorResponse {
    private String message;
    private int statusCode;
    private LocalDate timestamp;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Error> errors;
}
