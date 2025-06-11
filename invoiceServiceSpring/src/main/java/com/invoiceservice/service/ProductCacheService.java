package com.invoiceservice.service;

import com.invoiceservice.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductCacheService implements CommandLineRunner {

    private final Map<Integer, Product> productCache = new HashMap<>();
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String ALL_PRODUCTS_URL = "http://localhost:8083/api/products";

    @Override
    public void run(String... args) throws Exception {
        refreshProductCache();
    }

    //@Scheduled(fixedRate = 36) // Refresh every hour // check for one min
    public void refreshProductCache() {
        try {
            Product[] products = restTemplate.getForObject(ALL_PRODUCTS_URL, Product[].class);
            if (products != null) {
                productCache.clear();
                for (Product product : products) {
                    productCache.put(product.getProductId(), product);
                }
            }
        } catch (Exception e) {
            // Log error but don't fail startup
        }
    }

    public Product getProduct(Integer productId) {
        return productCache.get(productId);
    }
    
    public void clearCache() {
        productCache.clear();
    }
}