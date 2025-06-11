package com.invoiceservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Product {
    private Integer productId;
    private String name;
    private double price;
    private double taxPercent;
} 