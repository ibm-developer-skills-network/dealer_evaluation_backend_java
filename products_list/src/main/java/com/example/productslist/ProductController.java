package com.example.products_list;

import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Endpoint to get the list of products
    @CrossOrigin(origins = "*")
    @GetMapping("/products")
    public List<String> getProductsList() throws IOException {
        List<String> products = new ArrayList<>();
        
        // Load the file from classpath
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("products.json");
        // logger.info("Current classpath: " + System.getProperty("java.class.path"));

        if (inputStream == null) {
            throw new IOException("Unable to find products.json in classpath");
        }

        // Read the data from JSON file
        Map<String, Object> data = objectMapper.readValue(inputStream, Map.class);
        
        // Extract the product list from the loaded data
        List<Map<String, Object>> productList = (List<Map<String, Object>>) data.get("products");
        for (Map<String, Object> product : productList) {
            products.add((String) product.get("product"));
        }

        return products;
    }

    // Endpoint to get the dealers for a specific product
    @CrossOrigin(origins = "*")
    @GetMapping("/getdealers/{product}")
    public Object getDealers(@PathVariable String product) throws IOException {
        // Load the file from classpath
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("products.json");
        if (inputStream == null) {
            throw new IOException("Unable to find products.json in classpath");
        }

        // Read the data from JSON file
        Map<String, Object> data = objectMapper.readValue(inputStream, Map.class);

        // Find the dealers for the given product
        List<Map<String, Object>> productList = (List<Map<String, Object>>) data.get("products");
        for (Map<String, Object> productMeta : productList) {
            if (productMeta.get("product").equals(product)) {
                return productMeta.get("Dealers");
            }
        }

        // Return a message if no dealers are found
        return Map.of("message", "Could not find dealers for this product");
    }
}
