package com.andersen.java_intensive_13.hotel_app.handler;

import com.andersen.java_intensive_13.hotel_app.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(),
                ex.getMessage(), List.of(request.getDescription(false)));

        return new ResponseEntity<>(apiError,
                HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(ResourceProcessingException.class)
    public ResponseEntity<Object> handleResourceProcessingException(
            ResourceProcessingException ex, WebRequest request){
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),List.of(request.getDescription(false)));

        return new ResponseEntity<>(apiError,
                HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request){
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(),
                "Validation Field", details);

        return new ResponseEntity<>(apiError,
                HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(AlreadyReservedException.class)
    public ResponseEntity<Object> handleAlreadyReservedException(
            AlreadyReservedException existsException, WebRequest request){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(),
                existsException.getMessage(), List.of(request.getDescription(false)));

        return new ResponseEntity<>(apiError,
                HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(), List.of("Error processing request"));

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NotReservedException.class})
    public ResponseEntity<Object> handleNotReservedException(
            NotReservedException ex, WebRequest request){
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(), List.of(request.getDescription(false)));

        return new ResponseEntity<>(apiError,
                HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()));
    }
}