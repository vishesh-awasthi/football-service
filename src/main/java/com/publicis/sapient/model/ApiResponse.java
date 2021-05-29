package com.publicis.sapient.model;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {
    private List<T> data;
    private Error error;
    private boolean isErrorResponse;
}
