////This id not giving in start of the application
//
//package com.employeeservice.config;
//
//import jakarta.servlet.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class ApplicationSessionMdcFilter implements Filter {
//
//    private static final Logger log = LoggerFactory.getLogger(ApplicationSessionMdcFilter.class);
//    private static final String TX_ID_KEY = "NotInStartId";
//    private static final String APP_SESSION_ID = UUID.randomUUID().toString();
//
//    @Override
//    public void init(FilterConfig filterConfig) {
//        ensureMdc();
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        ensureMdc();
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void destroy() {
//        log.info("FirstSpringProjectApplication has ended successfully with Instance ID: {}", APP_SESSION_ID);
//    }
//
//    /**
//     * If txId not in MDC, add it and log the application start message.
//     */
//    private void ensureMdc() {
//        if (MDC.get(TX_ID_KEY) == null) {
//            MDC.put(TX_ID_KEY, APP_SESSION_ID);
//            log.info("Application session UUID set in MDC ({}): {}", TX_ID_KEY, APP_SESSION_ID);
//        }
//    }
//}
