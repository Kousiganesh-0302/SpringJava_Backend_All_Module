//package com.integratemodule.exception;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.time.LocalDateTime;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalAdviceException {
//    private static final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);
//    
//    //402 NotFound
//    @ExceptionHandler(EmployeeNotFoundException.class)
//    public ResponseEntity<Object> handleDataNotFoundException(EmployeeNotFoundException ex, WebRequest request) 
//	{
//
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//        body.put("error", "Not Found");
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }
//
//    //409 - Business Rule Violations
//    @ExceptionHandler(BusinessException.class)
//    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
//        log.error("Business error: {}", ex.getMessage());
//        
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.CONFLICT.value());
//        body.put("error", "Conflict");
//        body.put("message", ex.getMessage());
//       // body.put("path", request.getDescription(false).replace("uri=", ""));
//        
//        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
//    }
//
// // 500 - Server Errors
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleAllExceptions(
//            Exception ex) {
//        
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        body.put("error", "Internal Server Error");
//        body.put("message", "An unexpected error occurred");
//
//        log.error("Unexpected error: ", ex);
//        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}



package com.employeeservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestControllerAdvice
public class GlobalAdviceException {
    private static final Logger log = LoggerFactory.getLogger(GlobalAdviceException.class);

    //BAD Request 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("messages", errors);

        return ResponseEntity.badRequest().body(body);
    }


    
    // 404 - Employee Not Found
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Object> handleEmployeeNotFound(EmployeeNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Employee Not Found");
       // body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        log.error("Employee not found: {}", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // 409 - Business Rule Violation
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Business Rule Violation");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        log.error("Business rule violation: {}", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // // 500 - All Other Exceptions
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    //     Map<String, Object> body = new LinkedHashMap<>();
    //     body.put("timestamp", LocalDateTime.now());
    //     body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    //     body.put("error", "Internal Server Error");
    //     body.put("message", "An unexpected error occurred");
    //     body.put("path", request.getDescription(false).replace("uri=", ""));

    //     log.error("Unexpected error: ", ex);
    //     return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
}
