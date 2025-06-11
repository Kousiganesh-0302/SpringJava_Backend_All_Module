package com.invoiceservice.repo;

import com.invoiceservice.model.Invoice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber); 
    void deleteByInvoiceNumber(String invoiceNumber);
}