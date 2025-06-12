package com.invoiceservice.controller;

import com.invoiceservice.model.Invoice;
import com.invoiceservice.model.InvoiceRequest;
import com.invoiceservice.service.InvoiceServiceInterface;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/invoice")
@Tag(name = "Invoice", description = "Operations on invoice")
public class InvoiceController {

    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceServiceInterface invoiceService;

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        log.info("API: Creating new invoice for customer: {}", invoiceRequest.getCustomerId());
        Invoice invoice = invoiceService.createInvoice(invoiceRequest);
        log.info("API: Invoice created successfully with ID: {}", invoice.getInvoiceId());
        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
    }
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        log.info("API: Fetching all invoices for recent transactions.");
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }
    @GetMapping("/{invoiceId}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable String invoiceId) {
        log.info("API: Fetching invoice with ID: {}", invoiceId);
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/by-invoicenumber/{invoiceNumber}")
    public ResponseEntity<Invoice> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        log.info("API: Fetching invoice with number: {}", invoiceNumber);
        Invoice invoice = invoiceService.getInvoiceByNumber(invoiceNumber);
        return ResponseEntity.ok(invoice);
    }
    
    @PutMapping("/{invoiceId}")
    public ResponseEntity<Invoice> updateInvoice(
            @PathVariable String invoiceId,
            @RequestBody InvoiceRequest invoiceRequest) {
        log.info("Updating invoice {} …", invoiceId);
        Invoice updated = invoiceService.updateInvoice(invoiceId, invoiceRequest);
        return ResponseEntity.ok(updated);
    }
    
    @PutMapping("/by-invoicenumber/{invoiceNumber}")
    public ResponseEntity<Invoice> updateInvoiceByNumber(
            @PathVariable String invoiceNumber,
            @RequestBody InvoiceRequest invoiceRequest) {
        log.info("API: Updating invoice with number {}...", invoiceNumber);
        Invoice updated = invoiceService.updateInvoiceByNumber(invoiceNumber, invoiceRequest);
        return ResponseEntity.ok(updated);
    }

    
    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<String> deleteInvoice(@PathVariable String invoiceId) {
        log.info("API: Deleting invoice {} …", invoiceId);
        invoiceService.deleteInvoice(invoiceId);
        log.info("API: Invoice deletion processed for ID: {}", invoiceId);
      //  return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invoice with ID " + invoiceId + " deleted successfully.");
    }
    @DeleteMapping("/by-invoicenumber/{invoiceNumber}")
    public ResponseEntity<String> deleteInvoiceByNumber(@PathVariable String invoiceNumber) {
        log.info("API: Deleting invoice with number: {}", invoiceNumber);
        invoiceService.deleteInvoiceByNumber(invoiceNumber);
        log.info("API: Invoice deletion processed for ID: {}", invoiceNumber);
    //    return ResponseEntity.noContent().build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Invoice with Invoice Number " + invoiceNumber + " deleted successfully.");
    }
}