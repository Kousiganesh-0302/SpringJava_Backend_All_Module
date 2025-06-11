package com.saasant.customerServiceSpring.service;

import java.util.List;

import com.saasant.customerServiceSpring.vo.CustomerDetails;
import com.saasant.customerServiceSpring.entity.Customers;

public interface CustomerServiceInterface {
	
	CustomerDetails addCustomer(CustomerDetails customer);
	CustomerDetails updateCustomer(String customerId, CustomerDetails customer);
	void deleteCustomer(String customerId);
	CustomerDetails getCustomerById(String CustomerId);
	List<CustomerDetails> getAllCustomers();

}
