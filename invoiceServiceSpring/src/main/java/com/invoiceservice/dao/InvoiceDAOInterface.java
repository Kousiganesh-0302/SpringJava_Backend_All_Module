package com.invoiceservice.dao;

import java.util.List;

import com.invoiceservice.model.Invoice;
//import com.invoiceservice.model.InvoiceRequest;

public interface InvoiceDAOInterface {
    Invoice save(Invoice invoice);
    Invoice findById(String invoiceId);
    void deleteById(String invoiceId);
	Invoice findByInvoiceNumber(String invoiceNumber);
	void deleteByInvoiceNumber(String invoiceNumber);
    List<Invoice> findAll();

    }