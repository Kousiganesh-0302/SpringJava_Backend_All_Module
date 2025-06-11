package com.invoiceservice.service;

import com.invoiceservice.dao.InvoiceDAO;
import com.invoiceservice.exception.*;
import com.invoiceservice.model.Invoice;
import com.invoiceservice.model.InvoiceItem;
import com.invoiceservice.model.InvoiceRequest;
import com.invoiceservice.model.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class InvoiceService implements InvoiceServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);
    private final AtomicLong sequence = new AtomicLong(-1);
    
    @PersistenceContext
    private EntityManager em;

    @Value("${service.customer.url}")
    private String customerServiceUrl;
    
    @Value("${service.employee.url}")
    private String employeeServiceUrl;
    
//    @Value("${service.product.url}")
//    private String productServiceUrl;
//    
//    @Value("${invoice.number.format}")
//    private String invoiceNumberFormat;
    
    @Autowired
    private InvoiceDAO invoiceDAO;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ProductCacheService productCacheService;

    @Override
    @Transactional
    public Invoice createInvoice(InvoiceRequest invoiceRequest) {
        // Validate inputs
        if (invoiceRequest.getCustomerId() == null || invoiceRequest.getCustomerId().isEmpty()) {
            throw new IllegalArgumentException("Service: Customer ID is required");
        }
        
        if (invoiceRequest.getEmployeeId() == null || invoiceRequest.getEmployeeId().isEmpty()) {
            throw new IllegalArgumentException("Service: Employee ID is required");
        }
        
        if (invoiceRequest.getItems() == null || invoiceRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("Service: Invoice must contain at least one item");
        }
        
        // Validate customer and employee
        validateCustomer(invoiceRequest.getCustomerId());
        validateEmployee(invoiceRequest.getEmployeeId());
        
        // Create new invoice (don't set ID here)
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setCustomerId(invoiceRequest.getCustomerId());
        invoice.setEmployeeId(invoiceRequest.getEmployeeId());
        
        // Convert request items to invoice items
//        List<InvoiceItem> invoiceItems = invoiceRequest.getItems().stream()
//            .map(item -> {
//                Product product = productCacheService.getProduct(item.getProductId());
//                if (product == null) {
//                    throw new ProductNotFoundException("Product not found with ID: " + item.getProductId());
//                }
//                if (item.getQuantity() <= 0) {
//                    throw new IllegalArgumentException("Quantity must be positive for product ID: " + item.getProductId());
//                }
//                InvoiceItem invoiceItem = new InvoiceItem(product, item.getQuantity());
//                invoiceItem.setInvoice(invoice); // Set the relationship
//                return invoiceItem;
//            })
//            .collect(Collectors.toList());
        
        Map<Integer, Double> mergedQuantities = new HashMap<>();

        for (InvoiceRequest.Item item : invoiceRequest.getItems()) {
            double quantity = item.getQuantity();
            if (quantity <= 0) {
                throw new IllegalArgumentException("Service: Quantity must be positive for product ID: " + item.getProductId());
            }
            mergedQuantities.merge(item.getProductId(), quantity, Double::sum);
        }

        List<InvoiceItem> invoiceItems = mergedQuantities.entrySet().stream()
            .map(entry -> {
                Integer productId = entry.getKey();
                double totalQuantity = entry.getValue();

                Product product = productCacheService.getProduct(productId);
                if (product == null) {
                    throw new ProductNotFoundException("Service: Product not found with ID: " + productId);
                }

                InvoiceItem invoiceItem = new InvoiceItem(product, totalQuantity);
                invoiceItem.setInvoice(invoice);
                return invoiceItem;
            })
            .collect(Collectors.toList());

        
        invoice.setItems(invoiceItems);
        invoice.calculateTotals();
        
        // Let JPA handle the ID generation
        return invoiceDAO.save(invoice);
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public Invoice getInvoiceById(String invoiceId) {
        log.info("Service: Fetching invoice with ID: {}", invoiceId);
        Invoice invoice = invoiceDAO.findById(invoiceId);
        if (invoice == null) {
            log.error("Service: Invoice not found with ID: {}", invoiceId);
            throw new InvoiceNotFoundException("Service: Invoice not found with ID: " + invoiceId);
        }
        return invoice;
    }
    private void validateCustomer(String customerId) {
        try {
            String url = customerServiceUrl + customerId;
            restTemplate.getForObject(url, Object.class);
            log.debug("Service: Customer validation successful for ID: {}", customerId);
        } catch (Exception e) {
            log.error("Service: Customer not found: {}", customerId);
            throw new CustomerNotFoundException("Service: Customer not found with ID: " + customerId);
        }
    }
    
    private void validateEmployee(String employeeId) {
        try {
            String url = employeeServiceUrl + employeeId;
            restTemplate.getForObject(url, Object.class);
            log.debug("Service: Employee validation successful for ID: {}", employeeId);
        } catch (Exception e) {
            log.error("Service: Employee not found: {}", employeeId);
            throw new EmployeeNotFoundException("Service: Employee not found with ID: " + employeeId);
        }
    }
    
    private List<InvoiceItem> convertToInvoiceItems(List<InvoiceRequest.Item> requestItems) {
        return requestItems.stream().map(item -> {
            Product product = productCacheService.getProduct(item.getProductId());
            if (product == null) {
                throw new ProductNotFoundException("Service: Product not found with ID: " + item.getProductId());
            }
            return new InvoiceItem(product, item.getQuantity());
        }).collect(Collectors.toList());
    }
    
    private String generateInvoiceId() {
        return "INV-" + UUID.randomUUID().toString();
    }
    
//    private String generateInvoiceNumber() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String date = LocalDate.now().format(formatter);
//        long seq = sequence.incrementAndGet();
//        return String.format("INV-%s-%04d", date, seq);
//    }
    private String generateInvoiceNumber() {
        if (sequence.get() < 0) {
            // fire a one-off native SQL to grab the max suffix
            Object result = em.createNativeQuery(
                "SELECT CAST(MAX(SUBSTRING(invoice_number, -4)) AS UNSIGNED) FROM invoices"
            ).getSingleResult();
            long max = (result == null) ? 0L : ((Number) result).longValue();
            sequence.set(max);
        }

        long next = sequence.incrementAndGet();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("INV-%s-%04d", date, next);
    }
    
//    @Override
//    @Transactional
//    public Invoice updateInvoice(String invoiceId, InvoiceRequest req) {
//        Invoice existing = getInvoiceById(invoiceId);
//        // update allowed fields:
//        existing.setCustomerId(req.getCustomerId());
//        existing.setEmployeeId(req.getEmployeeId());
//        // rebuild items:
//        List<InvoiceItem> items = req.getItems().stream()
//            .map(i -> {
//                Product p = productCacheService.getProduct(i.getProductId());
//                InvoiceItem it = new InvoiceItem(p, i.getQuantity());
//                it.setInvoice(existing);
//                return it;
//            })
//            .collect(Collectors.toList());
//        existing.getItems().clear();
//        existing.getItems().addAll(items);
//        existing.calculateTotals();
//        return invoiceDAO.save(existing);
//    }
    
    @Override
    @Transactional
    public Invoice updateInvoice(String invoiceId, InvoiceRequest req) {
        Invoice existing = getInvoiceById(invoiceId);
        
        // Update allowed fields
        existing.setCustomerId(req.getCustomerId());
        existing.setEmployeeId(req.getEmployeeId());
        
        // Merge quantities for the same productId (like in createInvoice)
        Map<Integer, Double> mergedQuantities = new HashMap<>();
        for (InvoiceRequest.Item item : req.getItems()) {
            double quantity = item.getQuantity();
            if (quantity <= 0) {
                throw new IllegalArgumentException("Service: Quantity must be positive for product ID: " + item.getProductId());
            }
            mergedQuantities.merge(item.getProductId(), quantity, Double::sum);
        }

        // Rebuild invoice items with merged quantities
        List<InvoiceItem> items = mergedQuantities.entrySet().stream()
            .map(entry -> {
                Integer productId = entry.getKey();
                double totalQuantity = entry.getValue();

                Product product = productCacheService.getProduct(productId);
                if (product == null) {
                    throw new ProductNotFoundException("Service: Product not found with ID: " + productId);
                }

                InvoiceItem invoiceItem = new InvoiceItem(product, totalQuantity);
                invoiceItem.setInvoice(existing); // Set the relationship
                return invoiceItem;
            })
            .collect(Collectors.toList());

        // Clear existing items and add the new merged items
        existing.getItems().clear();
        existing.getItems().addAll(items);
        
        // Recalculate totals
        existing.calculateTotals();
        
        return invoiceDAO.save(existing);
    }

    @Override
    @Transactional
    public void deleteInvoice(String invoiceId) {
        // will throw if not found
        getInvoiceById(invoiceId);
        invoiceDAO.deleteById(invoiceId);
    }
    
    @Override
    @Transactional
    public Invoice updateInvoiceByNumber(String invoiceNumber, InvoiceRequest req) {
        Invoice existing = invoiceDAO.findByInvoiceNumber(invoiceNumber);
        if (existing == null) {
            throw new InvoiceNotFoundException("Service: Invoice not found with number: " + invoiceNumber);
        }
        return updateInvoice(existing.getInvoiceId(), req); // Reuse existing logic
    }
    
    @Override
    @Transactional
    public void deleteInvoiceByNumber(String invoiceNumber) {
    	Invoice existing = invoiceDAO.findByInvoiceNumber(invoiceNumber);
        if (existing == null) {
            throw new InvoiceNotFoundException("Service: Invoice not found with number: " + invoiceNumber);
        }
        invoiceDAO.deleteByInvoiceNumber(invoiceNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Invoice getInvoiceByNumber(String invoiceNumber) {
        log.info("Service: Fetching invoice by number: {}", invoiceNumber);
        Invoice invoice = invoiceDAO.findByInvoiceNumber(invoiceNumber);
        if (invoice == null) {
            throw new InvoiceNotFoundException("Service: Invoice not found with number: " + invoiceNumber);
        }
        return invoice;
    }
    
}