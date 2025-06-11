package com.example.productServiceSpring.model;

import jakarta.annotation.Generated;
import jakarta.persistence.*;

@Entity
@Table(name="products")
public class Product {
	
	@Id
	
    private int productId;
    private String name;
    private double price;
    private double taxPercent;

    public Product() {}

    public Product(int productId, String name, double price, double taxPercent) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.taxPercent = taxPercent;
    }

    // Getters and Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getTaxPercent() { return taxPercent; }
    public void setTaxPercent(double taxPercent) { this.taxPercent = taxPercent; }
}
