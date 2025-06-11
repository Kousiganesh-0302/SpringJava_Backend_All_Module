package com.saasant.customerServiceSpring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public CustomerNotFoundException(String id) {
        super(String.format("Customer with Id %s not found", id));
    }

}
