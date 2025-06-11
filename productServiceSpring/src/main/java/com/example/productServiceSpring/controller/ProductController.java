package com.example.productServiceSpring.controller;
import com.example.productServiceSpring.model.Product;
import com.example.productServiceSpring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
    @Autowired
    private ProductService productService;

    @PostMapping
    public String addProduct(@RequestBody Product product) {
        try {
            productService.addProduct(product);
            return "Product added successfully";
        } catch (Exception e) {  // Catch all exceptions
        	logger.error("Error adding product", e);
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping
    public List<Product> getAllProducts() throws Exception {
    	logger.info("Fetching all products");
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable("id") int productId, @RequestBody Product product) {
    	logger.info("Updating product with ID: {}", productId);
    	try {
            productService.updateProduct(productId, product);
            return "Product updated successfully";
        } catch (Exception e) {
        	logger.error("Error updating product", e);
            return "Error: " + e.getMessage();
        }
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") int productId) {
    	 logger.info("Deleting product with ID: {}", productId);
        try {
            productService.deleteProduct(productId);
            return "Product deleted successfully";
        } catch (Exception e) {
        	logger.error("Error deleting product", e);
            return "Error: " + e.getMessage();
        }
    }
}
