package com.invoiceservice.config;

import java.util.Arrays;
import java.util.Collections;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import com.invoiceservice.InvoiceServiceSpringApplication;
import static com.invoiceservice.config.InvoiceTrackingFilter.TRACKING_ID_HEADER;


@Configuration
public class RestTemplateConfig {  
	
    private static final String APP_ID_HEADER = "X-Application-ID";
//	private static final String TRACKING_ID_HEADER = "X-Tracking-ID";
//    private static final String TRACKING_ID_KEY = "trackId";
	@Bean
	public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            String trackingId = InvoiceTrackingFilter.getCurrentTrackingId();
            if (trackingId != null) {
                request.getHeaders().add(InvoiceTrackingFilter.TRACKING_ID_HEADER, trackingId);
            }
            request.getHeaders().add(APP_ID_HEADER, InvoiceServiceSpringApplication.APPLICATION_INSTANCE_ID);
            return execution.execute(request, body);
        };
        restTemplate.setInterceptors(Arrays.asList(interceptor));
        return restTemplate;
    }
	}