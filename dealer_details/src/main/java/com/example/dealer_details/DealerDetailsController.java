package com.example.dealer_details;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/api")
public class DealerDetailsController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Load dealers data from the JSON file
    private List<Map<String, Object>> loadDealersData() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dealers.json");
        if (inputStream == null) {
            throw new IOException("Unable to find dealers.json in classpath");
        }

        Map<String, Object> data = objectMapper.readValue(inputStream, Map.class);
        return (List<Map<String, Object>>) data.get("dealers");
    }

    // Endpoint: /price/:dealer/:product
    @GetMapping("/price/{dealer}/{product}")
    public ResponseEntity<Map<String, String>> getPrice(@PathVariable String dealer, @PathVariable String product) throws IOException {
        List<Map<String, Object>> dealers = loadDealersData();

        for (Map<String, Object> dealerData : dealers) {
            if (dealerData.get("Dealer").equals(dealer)) {
                Map<String, String> products = (Map<String, String>) dealerData.get("products");
                if (products != null && products.containsKey(product)) {
                    return ResponseEntity.ok(Map.of("message", product + " costs " + products.get(product) + " at " + dealer));
                }
                return ResponseEntity.ok(Map.of("message", product + " is not available with " + dealer));
            }
        }
        return ResponseEntity.ok(Map.of("message", "Dealer not found or the product is unavailable"));
    }

    // Endpoint: /allprice/:product
    @GetMapping("/allprice/{product}")
    public ResponseEntity<Object> getAllPrices(@PathVariable String product) throws IOException {
        List<Map<String, Object>> dealers = loadDealersData();
        List<Map<String, String>> pricesList = new ArrayList<>();

        for (Map<String, Object> dealerData : dealers) {
            Map<String, String> products = (Map<String, String>) dealerData.get("products");
            if (products != null && products.containsKey(product)) {
                pricesList.add(Map.of(
                        "Dealer", (String) dealerData.get("Dealer"),
                        "Price", products.get(product)
                ));
            }
        }

        if (!pricesList.isEmpty()) {
            return ResponseEntity.ok(Map.of("prices", pricesList));
        }
        return ResponseEntity.ok(Map.of("message", "The product is not available with any dealer"));
    }
}
