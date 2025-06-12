package com.invoiceservice.service;

import java.util.List;

import com.invoiceservice.model.Invoice;
import com.invoiceservice.model.InvoiceRequest;

public interface InvoiceServiceInterface {
    Invoice createInvoice(InvoiceRequest invoiceRequest);
    Invoice getInvoiceById(String invoiceId);
    Invoice updateInvoice(String invoiceId, InvoiceRequest invoiceRequest);
    void deleteInvoice(String invoiceId);
    Invoice getInvoiceByNumber(String invoiceNumber);
	Invoice updateInvoiceByNumber(String invoiceNumber, InvoiceRequest invoiceRequest);  
	void deleteInvoiceByNumber(String invoiceNumber); 
	List<Invoice> getAllInvoices();
}