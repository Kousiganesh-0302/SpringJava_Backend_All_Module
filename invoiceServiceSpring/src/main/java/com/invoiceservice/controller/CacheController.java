package com.invoiceservice.controller;

import com.invoiceservice.service.ProductCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @Autowired
    private ProductCacheService productCacheService;

    @PostMapping("/refresh-products")
    public ResponseEntity<String> refreshProductCache() {
        productCacheService.refreshProductCache();
        return ResponseEntity.ok("API: Product cache refreshed successfully");
    }

    @PostMapping("/clear-products")
    public ResponseEntity<String> clearProductCache() {
        productCacheService.clearCache();
        return ResponseEntity.ok("API: Product cache cleared successfully");
    }
}