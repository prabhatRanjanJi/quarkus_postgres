package com.fulfilment.application.monolith.products;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ProductTest {
    @Test
    void testConstructorSetsName() {
        String productName = "TestProduct";

        Product product = new Product(productName);

        assertEquals(productName, product.name);
    }
}
