package com.example.productServiceSpring.service;


import com.example.productServiceSpring.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {

    void addProduct(Product product) throws SQLException;

    List<Product> getAllProducts() throws SQLException;

    void updateProduct(int productId, Product product) throws SQLException;

    void deleteProduct(int productId) throws SQLException;
}
