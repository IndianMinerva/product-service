package com.im.productservice.service;

import com.im.productservice.entity.Product;

import java.util.List;

public interface ProductService {

    Product getProduct(Long id);

    List<Product> getProducts();

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long  id);

}
