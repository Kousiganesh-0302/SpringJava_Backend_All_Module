package com.saasant.customerServiceSpring.dao;
import com.saasant.customerServiceSpring.vo.CustomerDetails;
import com.saasant.customerServiceSpring.entity.Customers;
import java.util.List;
public interface CustomerDaoInterface {
	
	boolean addCustomer(CustomerDetails customer);
	CustomerDetails getCustomerById(String customerId);
	boolean updateCustomer(CustomerDetails customer);
	boolean deleteCustomer(String customerId);
	List<CustomerDetails> getAllCustomers();

}
