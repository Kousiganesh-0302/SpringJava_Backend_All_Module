package com.invoiceservice.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;
import com.invoiceservice.InvoiceServiceSpringApplication;

@Component
public class InvoiceTrackingFilter implements Filter {
    public static final String TRACKING_ID_HEADER = "X-Tracking-ID";
    private static final InheritableThreadLocal<String> currentTrackingId = new InheritableThreadLocal<>();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Ensure Application ID is in MDC for every request
        MDC.put(InvoiceServiceSpringApplication.APP_ID_KEY, InvoiceServiceSpringApplication.APPLICATION_INSTANCE_ID);

        // Extract or generate Tracking ID
        String trackingId = currentTrackingId.get();
        if (trackingId == null) {
            trackingId = request.getHeader(TRACKING_ID_HEADER);
        }
        if (trackingId == null || trackingId.isEmpty()) {
            trackingId = UUID.randomUUID().toString();
            response.setHeader(TRACKING_ID_HEADER, trackingId);
        }

        // Store in MDC and ThreadLocal
        currentTrackingId.set(trackingId);
        MDC.put(InvoiceServiceSpringApplication.TRACKING_ID_KEY, trackingId);

        try {
            chain.doFilter(request, response);
        } finally {
            // Clean up to avoid thread-local leaks
            MDC.remove(InvoiceServiceSpringApplication.TRACKING_ID_KEY);
            MDC.remove(InvoiceServiceSpringApplication.APP_ID_KEY);
            currentTrackingId.remove();
        }
    }

    public static String getCurrentTrackingId() {
        return currentTrackingId.get();
    }
}