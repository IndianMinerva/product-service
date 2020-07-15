package com.im.productservice.controller;

import com.im.productservice.BaseTestCase;
import com.im.productservice.entity.Product;
import com.im.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest implements BaseTestCase {

    private static final String URL = "http://localhost:%d/product";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductService productService;

    @Test
    void getProduct() {
        Product product = productService.createProduct(new Product("Name1", 10, 20, 10, "BRAND1"));
        ResponseEntity<Product> responseEntity = restTemplate.getForEntity(String.format(URL, port) + "/" +product.getId(), Product.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getProducts() {
        productService.createProduct(new Product("Name1", 10, 20, 10, "BRAND1"));
        productService.createProduct(new Product("Name2", 10, 20, 10, "BRAND1"));
        productService.createProduct(new Product("Name3", 10, 20, 10, "BRAND1"));

        ResponseEntity<List> responseEntity = restTemplate.getForEntity(String.format(URL, port) + "/all", List.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void test_createProduct() {
        ResponseEntity<Product> responseEntity = restTemplate.postForEntity(String.format(URL, port),
                new Product("Name", 10, 20, 10, "BRAND1"), Product.class);

        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void test_updateProduct() {
        Product createdProduct = productService.createProduct(new Product("Name", 10, 20, 10, "BRAND1"));
        createdProduct.setBrand("BRAND2");
        restTemplate.put(String.format(URL, port) + "/" + createdProduct.getId(), createdProduct);
    }

    @Test
    void deleteProduct() {
        Product createdProduct = productService.createProduct(new Product("Name", 10, 20, 10, "BRAND1"));
        restTemplate.delete(String.format(URL, port) + "/" + createdProduct.getId());
    }
}