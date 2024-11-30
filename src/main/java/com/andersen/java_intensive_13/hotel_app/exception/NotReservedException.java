package com.andersen.java_intensive_13.hotel_app.exception;

public class NotReservedException extends RuntimeException{
    public NotReservedException(String message) {
        super(message);
    }
}
