package com.im.productservice.service.impl;

import com.im.productservice.BaseTestCase;
import com.im.productservice.entity.Product;
import com.im.productservice.exceptions.EntityNotFoundException;
import com.im.productservice.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class ProductServiceImplTest implements BaseTestCase {

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService.createProduct(prepareTestProduct("name1", 10.0, 5.0, 10, "brand1"));
        productService.createProduct(prepareTestProduct("name2", 15.0, 5.0, 5, "brand2"));
    }

    @AfterEach
    void tearDown() {
        productService.getProducts().forEach(p -> productService.deleteProduct(p.getId()));
    }

    @Test
    void test_getProduct_returnCreatedProduct() {
        Product createdProduct = productService.createProduct(prepareTestProduct("XYZ", 2.5f, 1.0f, 4, "brand3"));
        Product returnedProduct = productService.getProduct(createdProduct.getId());

        Assertions.assertEquals(createdProduct.getName(), returnedProduct.getName());
        Assertions.assertEquals(createdProduct.getPrice(), returnedProduct.getPrice());
        Assertions.assertEquals(createdProduct.getOldPrice(), returnedProduct.getOldPrice());
        Assertions.assertEquals(createdProduct.getStock(), returnedProduct.getStock());
        Assertions.assertEquals(createdProduct.getBrand(), returnedProduct.getBrand());
    }

    @Test
    void test_getProduct_throws_exception() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> productService.getProduct(1234L));
    }

    @Test
    void test_getProducts_returns_products() {
        List<Product> products = productService.getProducts();
        Assertions.assertEquals(2, products.size());
    }

    @Test
    void createProduct() {
        Product product = productService.createProduct(prepareTestProduct("name1", 10.5, 11.5, 12, "BRAND1"));
        Assertions.assertNotNull(product.getId());
    }

    @Test
    void test_updateProduct() {
        String updatedBrand = "BRAND2";
        String updatedName = "NAME2";
        double updatedPrice = 100.5;
        double updatedOldPrice = 110.5;
        int updatedStock = 10;

        Product product = productService.createProduct(prepareTestProduct("name1", 10.5, 11.5, 12, "BRAND1"));
        product.setBrand(updatedBrand);
        product.setName(updatedName);
        product.setPrice(updatedPrice);
        product.setOldPrice(updatedOldPrice);
        product.setStock(updatedStock);

        Product updatedProduct = productService.updateProduct(product.getId(), product);

        Assertions.assertEquals(updatedBrand, updatedProduct.getBrand());
        Assertions.assertEquals(updatedName, updatedProduct.getName());
        Assertions.assertEquals(updatedPrice, updatedProduct.getPrice());
        Assertions.assertEquals(updatedOldPrice, updatedProduct.getOldPrice());
        Assertions.assertEquals(updatedStock, updatedProduct.getStock());
    }

    @Test
    void test_updateProduct_throws_exception() {
        String updatedBrand = "BRAND2";
        String updatedName = "NAME2";
        double updatedPrice = 100.5;
        double updatedOldPrice = 110.5;
        int updatedStock = 10;
        Long updatedId = 123456L;

        Product product = productService.createProduct(prepareTestProduct("name1", 10.5, 11.5, 12, "BRAND1"));
        product.setBrand(updatedBrand);
        product.setName(updatedName);
        product.setPrice(updatedPrice);
        product.setOldPrice(updatedOldPrice);
        product.setStock(updatedStock);

        Assertions.assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(updatedId, product));
    }

    @Test
    void test_deleteProduct() {
        Product product = productService.createProduct(prepareTestProduct("name1", 10.5, 11.5, 12, "BRAND1"));
        productService.deleteProduct(product.getId());
        productService.deleteProduct(product.getId());

        Assertions.assertThrows(EntityNotFoundException.class, () -> productService.getProduct(product.getId()));
    }

    private Product prepareTestProduct(String name, double price, double oldPrice, int stock, String brand) {
        Product product = new Product(); //ProductBuilder??
        product.setName(name);
        product.setPrice(price);
        product.setPrice(oldPrice);
        product.setStock(stock);
        product.setBrand(brand);

        return product;
    }
}