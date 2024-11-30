package com.andersen.java_intensive_13.hotel_app.exception;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ApiError {
    private int status;
    private String message;
    private List<String> details;
}