package com.saasant.customerServiceSpring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "customers")
@Getter @Setter @ToString
public class Customers {
	
	//'customer_id','varchar(20)','NO','PRI',NULL,''
	//'customer_name','varchar(20)','NO','',NULL,''
	//'customer_mobile','varchar(12)','YES','',NULL,''
	//'customer_location','varchar(50)','YES','',NULL,''
	
//	http://localhost:8081/api/customers
//	{
//	    "customerId": "CUST101",
//	    "customerName": "Divya M",
//	    "mobileNumber": "9876543210",
//	    "customerLocation": "Bangalore"
//	}
	
	@Id
	private String customerId;
	private String customerName;
    @Column(name = "customer_mobile")
	private String mobileNumber;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getCustomerLocation() {
		return customerLocation;
	}
	public void setCustomerLocation(String customerLocation) {
		this.customerLocation = customerLocation;
	}
	private String customerLocation;


}
