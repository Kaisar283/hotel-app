package com.andersen.java_intensive_13.hotel_app.exception;

public class AlreadyReservedException extends RuntimeException{
    public AlreadyReservedException(String message) {
        super(message);
    }
}
