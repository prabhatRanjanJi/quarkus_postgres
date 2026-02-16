package com.fulfilment.application.monolith.products;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestHTTPEndpoint(ProductResource.class)
public class ProductResourceTest {

    @Inject
    ProductRepository productRepository;

    @BeforeEach
    @Transactional
    void setup() {
        productRepository.deleteAll();

        Product p = new Product();
        p.name = "Test";
        p.description = "Initial Product";
        p.price = BigDecimal.valueOf(50);
        p.stock = 10;
        productRepository.persist(p);
        System.out.println("Generated ID for setup product: " + p.id);

    }

    @Test
    void getSingle_productExists() {
        Product persisted = productRepository.find("name", "Test").firstResult();
        given()
                .when().get("/" + persisted.id)
                .then()
                .statusCode(200)
                .body("name", is("Test"))
                .body("description", is("Initial Product"))
                .body("price", is(50))
                .body("stock", is(10));
    }

    @Test
    void createProduct_success() {
        Product newProduct = new Product();
        newProduct.name = "NEW_PRODUCT";
        newProduct.description = "New Description";
        newProduct.price = BigDecimal.valueOf(100);
        newProduct.stock = 20;

        given()
                .contentType("application/json")
                .body(newProduct)
                .when().post()
                .then()
                .statusCode(201)
                .body("name", is("NEW_PRODUCT"))
                .body("description", is("New Description"))
                .body("price", is(100))
                .body("stock", is(20));
    }

    @Test
    void createProduct_withId_shouldFail() {
        Product invalid = new Product();
        invalid.id = 99L; // manually set ID
        invalid.name = "INVALID";

        given()
                .contentType("application/json")
                .body(invalid)
                .when().post()
                .then()
                .statusCode(422)
                .body("error", is("Id was invalidly set on request."));
    }

    @Test
    void updateProduct_success() {
        Product persisted = productRepository.find("name", "Test").firstResult();
        Product updated = new Product();
        updated.name = "UPDATED";
        updated.description = "Updated Desc";
        updated.price = BigDecimal.valueOf(200);
        updated.stock = 30;

        given()
                .contentType("application/json")
                .body(updated)
                .when().put("/" + persisted.id)
                .then()
                .statusCode(200)
                .body("name", is("UPDATED"))
                .body("description", is("Updated Desc"))
                .body("price", is(200))
                .body("stock", is(30));
    }

    @Test
    void updateProduct_missingName_shouldFail() {
        Product updated = new Product();
        updated.description = "No name";

        given()
                .contentType("application/json")
                .body(updated)
                .when().put("/1")
                .then()
                .statusCode(422)
                .body("error", is("Product Name was not set on request."));
    }

    @Test
    void updateProduct_notFound_shouldFail() {
        Product updated = new Product();
        updated.name = "NOT_FOUND";

        given()
                .contentType("application/json")
                .body(updated)
                .when().put("/99")
                .then()
                .statusCode(404)
                .body("error", is("Product with id of 99 does not exist."));
    }

    @Test
    void deleteProduct_success() {
        Product persisted = productRepository.find("name", "Test").firstResult();
        given()
                .when().delete("/" + persisted.id)
                .then()
                .statusCode(204);
    }

    @Test
    void deleteProduct_notFound_shouldFail() {
        given()
                .when().delete("/99")
                .then()
                .statusCode(404)
                .body("error", is("Product with id of 99 does not exist."));
    }
}
