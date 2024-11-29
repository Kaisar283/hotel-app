package com.andersen.java_intensive_13.hotel_app.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiError {
    private int status;
    private String message;
    private List<String> details;

    public ApiError(int status, String message, List<String> details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }
}