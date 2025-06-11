package com.invoiceservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;

import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({CustomerNotFoundException.class, 
        EmployeeNotFoundException.class, 
        ProductNotFoundException.class,
        InvoiceNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFoundException(RuntimeException ex) {
          log.error("Not found exception: {}", ex.getMessage());
          Map<String, String> errorResponse = new HashMap<>();
          errorResponse.put("error", ex.getMessage());
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
}   

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
//        log.error("Unexpected error: ", ex);
//        Map<String, String> errorResponse = new HashMap<>();
//        errorResponse.put("error", "An unexpected error occurred");
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//    }
}