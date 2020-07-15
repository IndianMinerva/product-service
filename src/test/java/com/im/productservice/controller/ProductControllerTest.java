package com.im.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.im.productservice.BaseTestCase;
import com.im.productservice.entity.Product;
import com.im.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest implements BaseTestCase {

    private static final String URL = "http://localhost:%d/product";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Test
    void test_getProduct_success() throws Exception {
        Product createdProduct = productService.createProduct(new Product("Name", 10, 20, 10, "BRAND1"));
        mockMvc.perform(MockMvcRequestBuilders.get("/product/all")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/all")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void test_createProduct() throws Exception {
        Product createdProduct = productService.createProduct(new Product("Name", 10, 20, 10, "BRAND1"));
        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .content(OBJECT_MAPPER.writeValueAsString(createdProduct))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void test_updateProduct_success() throws Exception {
        Product createdProduct = productService.createProduct(new Product("Name", 10, 20, 10, "BRAND1"));
        mockMvc.perform(MockMvcRequestBuilders.put("/product/" + createdProduct.getId())
                .content(OBJECT_MAPPER.writeValueAsString(createdProduct))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    void test_updateProduct_failure() throws Exception {
        Product createdProduct = productService.createProduct(new Product("Name", 10, 20, 10, "BRAND1"));
        createdProduct.setBrand("BRAND2");
        mockMvc.perform(MockMvcRequestBuilders.put("/product/1234")
                .content(OBJECT_MAPPER.writeValueAsString(createdProduct))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteProduct() throws Exception {
        Product createdProduct = productService.createProduct(new Product("Name", 10, 20, 10, "BRAND1"));
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/" + createdProduct.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}