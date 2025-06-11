package com.saasant.customerServiceSpring.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;
import com.saasant.customerServiceSpring.CustomerServiceSpringApplication;

@Component
public class MdcLoggingFilter implements Filter{
	
	private static final String LOGGING_ID_KEY = "transactionId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
    	try {
            MDC.put(LOGGING_ID_KEY, CustomerServiceSpringApplication.APPLICATION_INSTANCE_ID);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(LOGGING_ID_KEY);
        }
    }

}

