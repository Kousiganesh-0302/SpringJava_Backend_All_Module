package com.invoiceservice.dao;

import com.invoiceservice.model.Invoice;
import com.invoiceservice.repo.InvoiceRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceDAO implements InvoiceDAOInterface {

    private static final Logger log = LoggerFactory.getLogger(InvoiceDAO.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Invoice save(Invoice invoice) {
        if (invoice.getInvoiceId() == null) {
            entityManager.persist(invoice);
            return invoice;
        } else {
            return entityManager.merge(invoice);
        }
    }

    @Override
    public Invoice findById(String invoiceId) {
        log.debug("DAO: Finding invoice by ID: {}", invoiceId);
        return invoiceRepository.findById(invoiceId).orElse(null);
    }
    
    @Override
    public void deleteById(String invoiceId) {
        invoiceRepository.deleteById(invoiceId);
    }
    
    @Override
    public Invoice findByInvoiceNumber(String invoiceNumber) {
        log.debug("DAO: Finding invoice by number: {}", invoiceNumber);
        return invoiceRepository.findByInvoiceNumber(invoiceNumber).orElse(null);
    }
    
    @Override
    public void deleteByInvoiceNumber(String invoiceNumber) {
        log.debug("DAO: Attempting to delete invoice by number: {}", invoiceNumber);
        invoiceRepository.deleteByInvoiceNumber(invoiceNumber);
        log.debug("DAO: Successfully deleted invoice by number: {}", invoiceNumber);

    }

    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }
}