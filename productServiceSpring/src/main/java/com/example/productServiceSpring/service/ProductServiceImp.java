package com.example.productServiceSpring.service;

import com.example.productServiceSpring.model.Product;
import com.example.productServiceSpring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductServiceImp implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addProduct(Product product) {
        logger.info("Adding product: {}", product.getName());
        productRepository.save(product);
        logger.debug("Product saved successfully");
    }

    @Override
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll();
    }

    @Override
    public void updateProduct(int productId, Product product) {
        logger.info("Updating product with ID: {}", productId);
        Optional<Product> existing = productRepository.findById(productId);
        if (existing.isPresent()) {
            product.setProductId(productId);
            productRepository.save(product);
            logger.debug("Product updated successfully");
        } else {
            logger.error("Product with ID {} not found for update", productId);
            throw new RuntimeException("Product not found with id " + productId);
        }
    }

    @Override
    public void deleteProduct(int productId) {
        logger.info("Deleting product with ID: {}", productId);
        if (!productRepository.existsById(productId)) {
            logger.error("Product with ID {} not found for deletion", productId);
            throw new RuntimeException("Product not found with id " + productId);
        }
        productRepository.deleteById(productId);
        logger.debug("Product deleted successfully");
    }
}


