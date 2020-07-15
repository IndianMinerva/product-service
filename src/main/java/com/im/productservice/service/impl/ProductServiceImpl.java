package com.im.productservice.service.impl;

import com.im.productservice.entity.Product;
import com.im.productservice.exceptions.EntityNotFoundException;
import com.im.productservice.repository.ProductRepository;
import com.im.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.couldNotFindEntity(Product.class, id));
    }

    @Override
    public List<Product> getProducts() {
        return stream(productRepository.findAll().spliterator(), false).collect(toList());
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product currentProduct = getProduct(id);
        BeanUtils.copyProperties(product, currentProduct, "id");
        return productRepository.save(currentProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("Error deleting the product {} from the system", id, e);
        }
    }
}
