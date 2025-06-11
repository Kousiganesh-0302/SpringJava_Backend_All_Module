package com.saasant.customerServiceSpring.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository; // Changed from @Service to @Repository

import com.saasant.customerServiceSpring.entity.Customers;
import com.saasant.customerServiceSpring.repo.CustomerRepository;
import com.saasant.customerServiceSpring.vo.CustomerDetails;

import jakarta.transaction.Transactional;

@Repository 
public class CustomerDao implements CustomerDaoInterface {

    private static final Logger log = LoggerFactory.getLogger(CustomerDao.class);

    @Autowired
    CustomerRepository customerRepository;

    // Helper method to convert Entity to VO
    private CustomerDetails convertToDetails(Customers customerEntity) {
        if (customerEntity == null) {
            return null;
        }
        return new CustomerDetails(customerEntity.getCustomerId(),customerEntity.getCustomerName(),customerEntity.getMobileNumber(),customerEntity.getCustomerLocation());
    }

    // Helper method to convert VO to Entity
    private Customers convertToEntity(CustomerDetails customerDetails) {
        if (customerDetails == null) {
            return null;
        }
        Customers customerEntity = new Customers();
        customerEntity.setCustomerId(customerDetails.getCustomerId());
        customerEntity.setCustomerName(customerDetails.getCustomerName());
        customerEntity.setMobileNumber(customerDetails.getMobileNumber());
        customerEntity.setCustomerLocation(customerDetails.getCustomerLocation());
        return customerEntity;
    }
    
    @Transactional
    @Override
    public boolean addCustomer(CustomerDetails customerDetails) {
        log.debug("DAO: Attempting to add customer: {}", customerDetails.getCustomerId());
        if (customerRepository.existsById(customerDetails.getCustomerId())) {
            log.warn("DAO: Customer ID {} already exists. Cannot add.", customerDetails.getCustomerId());
            return false;
        }
        Customers customerEntity = convertToEntity(customerDetails);
        customerRepository.save(customerEntity);
        log.info("DAO: Successfully added customer {} via repository.", customerDetails.getCustomerId());
        return true;
    }

    @Override
    public CustomerDetails getCustomerById(String customerId) {
        log.debug("DAO: Fetching customer by ID: {} via repository", customerId);
        Customers customerEntity = customerRepository.findById(customerId).orElse(null);
        if (customerEntity != null) {
            log.debug("DAO: Customer found for ID: {}", customerId);
            return convertToDetails(customerEntity);
        } else {
            log.debug("DAO: No customer found for ID: {}", customerId);
            return null;
        }
    }
    
    @Transactional
    @Override
    public boolean updateCustomer(CustomerDetails customerDetails) {
        log.debug("DAO: Attempting to update customer: {} via repository", customerDetails.getCustomerId());
        Customers existingEntity = customerRepository.findById(customerDetails.getCustomerId()).orElse(null);
        if (existingEntity != null) {
            existingEntity.setCustomerName(customerDetails.getCustomerName());
            existingEntity.setMobileNumber(customerDetails.getMobileNumber());
            existingEntity.setCustomerLocation(customerDetails.getCustomerLocation());
            customerRepository.save(existingEntity);
            log.info("DAO: Successfully updated customer {}.", customerDetails.getCustomerId());
            return true;
        } else {
            log.warn("DAO: No customer found with ID {} to update.", customerDetails.getCustomerId());
            return false;
        }
    }
    
    @Transactional
    @Override
    public boolean deleteCustomer(String customerId) {
        log.debug("DAO: Attempting to delete customer: {} via repository", customerId);
        if (customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);
            log.info("DAO: Successfully deleted customer {}.", customerId);
            return true;
        } else {
            log.warn("DAO: No customer found with ID {} to delete.", customerId);
            return false;
        }
    }

    @Override
    public List<CustomerDetails> getAllCustomers() {
        log.debug("DAO: Fetching all customers via repository.");
        List<Customers> entities = customerRepository.findAll();
        List<CustomerDetails> detailsList = new ArrayList<>();
        for(Customers entity : entities) {
        	detailsList.add(this.convertToDetails(entity));
        }
        log.debug("DAO: Retrieved {} customers.", detailsList.size());
        return detailsList;
    }

}