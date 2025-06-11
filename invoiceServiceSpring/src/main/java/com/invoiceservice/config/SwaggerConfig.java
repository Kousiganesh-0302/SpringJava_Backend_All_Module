package com.invoiceservice.config;

import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI employeeServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Invoice Service API")
                        .description("API for managing Invoice records creation")
                        .version("v1.0"));
//                        .contact(new Contact()
//                                .name("API Support")
//                                .email("support@employeeservice.com")
//                                .url("http://employeeservice.com"))
//                        .license(new License()
//                                .name("Apache 2.0")
//                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}