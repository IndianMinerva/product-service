package com.im.productservice;

import com.im.productservice.entity.Product;
import com.im.productservice.repository.ProductRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.util.stream.Collectors.toList;

@SpringBootApplication
public class ProductServiceApplication {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public ApplicationRunner initializer(ProductRepository repository) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:product_data.csv");
        CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(resource.getInputStream())));
        reader.readNext(); //skip header
        repository.saveAll(reader.readAll().stream().map(CsvToProductMapper::mapToProduct).collect(toList()));

        return args -> repository.saveAll(Arrays.asList());
    }
}

class CsvToProductMapper {
    static Product mapToProduct(String[] csvRecord) {
        return new Product(parseLong(csvRecord[0]), csvRecord[1], parseFloat(csvRecord[2]), parseFloat(csvRecord[3]), parseInt(csvRecord[4]), csvRecord[5]);
    }
}