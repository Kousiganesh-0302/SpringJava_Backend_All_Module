package com.invoiceservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoice_items")
@Data
@Getter 
@Setter
public class InvoiceItem {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
	private Long id;
    
	@ManyToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")
	@JsonIgnore
    private Invoice invoice;
    
    @Column(name = "product_id")
    private Integer productId;
    
    @Column(name = "product_name")
    private String productName;
    private double price;
    
    @Column(name = "tax_percent")
    private double taxPercentage;
    private double quantity;
    private double subtotal;
    
    @Column(name = "tax_amount")
    private double taxAmount;
    private double total;
    
//    @Transient
//    private Product product;
    
//    @Version
//    @Column(name = "version")
//    private Long version = 0L;
    
public InvoiceItem() {}
    
    public InvoiceItem(Product product, double quantity) {
        this.productId = product.getProductId();
        this.productName = product.getName();
        this.price = product.getPrice();
        this.taxPercentage = product.getTaxPercent();
        this.quantity = quantity;
  //      this.product = product;
        calculateAmounts();
    }
    
    public void calculateAmounts() {
        this.subtotal = price * quantity;
        this.taxAmount = subtotal * (taxPercentage / 100);
        this.total = subtotal + taxAmount;
    }
//    @PrePersist
//    protected void onCreate() {
//        if (version == null) {
//            version = 0L;
//        }
//    }
}