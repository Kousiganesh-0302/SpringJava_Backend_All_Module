package com.example.productServiceSpring.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import java.io.IOException;
import java.util.UUID;

public class TrackingIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String trackingId = UUID.randomUUID().toString(); // or get from request header
        MDC.put("trackingId", trackingId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("trackingId");
        }
    }
}
