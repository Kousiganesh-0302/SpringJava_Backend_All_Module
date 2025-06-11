package com.invoiceservice;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing

public class InvoiceServiceSpringApplication {

	// Static Application ID (generated once at startup)
    public static final String APPLICATION_INSTANCE_ID = UUID.randomUUID().toString();	// MDC Keys
    
    // MDC Keys
    public static final String APP_ID_KEY = "appId";
    public static final String TRACKING_ID_KEY = "trackId";
    
    public static void main(String[] args) {
        MDC.put(InvoiceServiceSpringApplication.APP_ID_KEY, APPLICATION_INSTANCE_ID);
		SpringApplication.run(InvoiceServiceSpringApplication.class, args);
		System.out.println("Invoice Service Started with:");
        System.out.println("Application ID: " + APPLICATION_INSTANCE_ID);
	}

}
