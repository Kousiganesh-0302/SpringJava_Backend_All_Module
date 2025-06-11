package com.employeeservice.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import com.employeeservice.EmployeeServiceApplication;

//@Component
//public class HttpIdPasser implements Filter{
//	
//	private static final String LOGGING_ID_KEY = "txId";
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
//            throws IOException, ServletException {
//    	try {
//            MDC.put(LOGGING_ID_KEY, EmployeeServiceApplication.APPLICATION_INSTANCE_ID);
//            filterChain.doFilter(request, response);
//        } finally {
//            MDC.remove(LOGGING_ID_KEY);
//        }
//    	
//    }
//}



@Component
public class EmployeeTrackingFilter implements Filter {
    private static final String TRACKING_ID_HEADER = "X-Tracking-ID";
    private static final String TRACKING_ID_KEY = "trackId";
    private static final String MODULE_ID_KEY = "moduleId"; // Static per service

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        String trackingId = request.getHeader(TRACKING_ID_HEADER);

        // Set MDC for logging
        MDC.put(TRACKING_ID_KEY, trackingId); // From invoice service
        MDC.put(MODULE_ID_KEY, EmployeeServiceApplication.APPLICATION_INSTANCE_ID); // Local module ID

        try {
            chain.doFilter(req, res);
        } finally {
            MDC.remove(TRACKING_ID_KEY); // Clean up
        }
    }
}