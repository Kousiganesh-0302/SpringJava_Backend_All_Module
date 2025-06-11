package com.invoiceservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class InvoiceRequest {
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    @NotBlank(message = "Employee ID is required")
    private String employeeId;
    
    @NotEmpty(message = "Invoice must contain at least one item")
    private List<Item> items;
    
    @Getter
    @Setter
    public static class Item {
        @NotNull(message = "Product ID is required")
        private Integer productId;
        
        @Min(value = 1, message = "Quantity must be at least 1")
        private double quantity;
    }
}