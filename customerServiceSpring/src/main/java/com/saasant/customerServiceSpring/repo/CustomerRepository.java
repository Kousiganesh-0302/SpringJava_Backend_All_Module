package com.saasant.customerServiceSpring.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.saasant.customerServiceSpring.entity.Customers;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, String> {
}
