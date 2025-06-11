package com.employeeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
@SpringBootApplication
public class EmployeeServiceApplication {
	private static final Logger log = LoggerFactory.getLogger(EmployeeServiceApplication.class);
    public static final String APPLICATION_INSTANCE_ID = UUID.randomUUID().toString();
    private static final String LOGGING_ID_KEY = "moduleId";

	public static void main(String[] args) {
		if (MDC.get(LOGGING_ID_KEY) == null) {
            MDC.put(LOGGING_ID_KEY, APPLICATION_INSTANCE_ID);
        }
        log.info("Starting EmployeeServiceSpringApplication with Instance ID: {}", APPLICATION_INSTANCE_ID);

		SpringApplication.run(EmployeeServiceApplication.class, args);
		
        log.info("EmployeeServiceApplication has started successfully with Instance ID: {}", APPLICATION_INSTANCE_ID);

	}

}


