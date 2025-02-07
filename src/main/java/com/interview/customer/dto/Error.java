package com.interview.customer.dto;

import lombok.Data;

@Data
public class Error {
    private String field;
    private String location;
    private String reason;
}
