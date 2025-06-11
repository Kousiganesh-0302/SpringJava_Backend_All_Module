package com.invoiceservice.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "invoices")
public class Invoice {

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "invoice_id")
    private String invoiceId;
	
    @Column(name = "invoice_number", unique = true)   
    private String invoiceNumber;
    
    @Column(name = "customer_id", length = 255)
    private String customerId;
    
    @Column(name = "employee_id", length = 255)
    private String employeeId;
    private double subtotal;
    private double tax;
    private double total;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false )
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(name = "last_modified_date", updatable = false)
    private LocalDateTime lastModifiedDate;
    
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items;
    
//    @Version
//    @Column(name = "version")
//    private Long version = 0L;
    
    // Getters and Setters
    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    public void calculateTotals() {
        if (items != null) {
            this.subtotal = items.stream().mapToDouble(InvoiceItem::getSubtotal).sum();
            this.tax = items.stream().mapToDouble(InvoiceItem::getTaxAmount).sum();
            this.total = subtotal + tax;
        }
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
//    @PrePersist
//    protected void onCreate() {
//        if (version == null) {
//            version = 0L;
//        }
//    }
//
//	public void setVersion(long l) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public Object getVersion() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}