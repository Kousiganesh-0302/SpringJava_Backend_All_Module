package com.saasant.customerServiceSpring.service;
import com.saasant.customerServiceSpring.vo.CustomerDetails;
// import com.saasant.firstSpringProject.entity.Customers; // Not directly used now
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saasant.customerServiceSpring.CustomerServiceSpringApplication;
import com.saasant.customerServiceSpring.dao.CustomerDaoInterface;
import com.saasant.customerServiceSpring.exception.CustomerNotFoundException;

@Service
public class CustomerService implements CustomerServiceInterface {
	
	private final CustomerDaoInterface customerDao;
	private static final Logger log = LoggerFactory.getLogger(CustomerServiceSpringApplication.class);
	
	@Autowired
	public CustomerService(CustomerDaoInterface customerDao){ // Constructor injection for DAO
		this.customerDao = customerDao;
	}
	
	@Override
	public CustomerDetails addCustomer(CustomerDetails customer) {
		log.info("SERVICE: Attempting to add customer: {}", customer != null ? customer.getCustomerId() : "null");
		if (customer == null || customer.getCustomerId() == null || customer.getCustomerId().trim().isEmpty()) {
			return null;
		}
		boolean success = customerDao.addCustomer(customer); // Calls DAO
		if(success) {
		    log.info("SERVICE: Customer {} added successfully via DAO.", customer.getCustomerId());
			return customerDao.getCustomerById(customer.getCustomerId()); // Fetch the added customer details
		}
		log.error("SERVICE: Failed to add customer using DAO: {}", customer.getCustomerId());
		return null;
	}

	@Override
	public CustomerDetails updateCustomer(String customerId, CustomerDetails customerUpdates) {
		log.info("SERVICE: Attempting to update customer with ID: {}", customerId);
		if (customerId == null || customerId.trim().isEmpty() || customerUpdates == null) {
            log.warn("SERVICE: Customer ID for update or customer data cannot be null or empty.");
            return null; 
        }
		
        CustomerDetails existingCustomer = customerDao.getCustomerById(customerId); // Check existence via DAO
        if (existingCustomer == null) {
        	log.warn("SERVICE: Customer not found with ID: {}. Cannot update.", customerId);
            throw new CustomerNotFoundException(customerId);
        }

        customerUpdates.setCustomerId(customerId); // Ensure ID is set for the update object
        
        boolean success = customerDao.updateCustomer(customerUpdates); // Calls DAO
        if (success) {
        	log.info("SERVICE: Customer {} updated successfully via DAO.", customerId);
        	return customerDao.getCustomerById(customerId); // Return updated details
        }
        log.error("SERVICE: Failed to update customer using DAO: {}", customerId);
        return null;
	}

	@Override
	public void deleteCustomer(String customerId) {
		log.info("SERVICE: Attempting to delete customer with ID: {}", customerId);
		if (customerId == null || customerId.trim().isEmpty()) {
			log.warn("SERVICE: Customer ID for deletion cannot be null or empty.");
            return; // Or throw IllegalArgumentException
        }
		
		CustomerDetails existingCustomer = customerDao.getCustomerById(customerId); // Check existence via DAO
        if (existingCustomer == null) {
            log.warn("SERVICE: Customer not found with ID: {}. Cannot delete.", customerId);
            throw new CustomerNotFoundException(customerId);
        }
        if (customerDao.deleteCustomer(customerId)) { // Calls DAO
        	log.info("SERVICE: Customer {} deleted successfully via DAO.", customerId);
        } else {
        	log.warn("SERVICE: Failed to delete customer or customer not found with ID {} via DAO.", customerId);
        }
	}
	
	@Override
	public List<CustomerDetails> getAllCustomers(){ 
		log.info("SERVICE: Fetching all customers via DAO.");
		List<CustomerDetails> customers = customerDao.getAllCustomers(); // Calls DAO
		log.debug("SERVICE: Found {} customers.", customers.size());
		return customers;
	}

	@Override
	public CustomerDetails getCustomerById(String customerId) {
		log.info("SERVICE: Fetching customer by ID: {}", customerId);
		 if (customerId == null || customerId.trim().isEmpty()) {
			 log.warn("SERVICE: Customer ID for retrieval cannot be null or empty.");
            return null;
        }
		CustomerDetails customer = customerDao.getCustomerById(customerId); // Calls DAO
		if(customer == null) {
			log.warn("SERVICE: No customer found with ID: {}",customerId);
			throw new CustomerNotFoundException(customerId);
		}
		return customer;
	}
	
}