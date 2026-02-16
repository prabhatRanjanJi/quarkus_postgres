package com.fulfilment.application.monolith.stores;

import com.fulfilment.application.monolith.stores.service.StoreService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@QuarkusTest
class StoreResourceTest {

    @InjectMock
    StoreService storeService;

    @Test
    void shouldReturnAllStores() {
        Store store1 = new Store();
        store1.id = 1L;
        store1.name = "Store-A";
        store1.quantityProductsInStock = 10;

        Store store2 = new Store();
        store2.id = 2L;
        store2.name = "Store-B";
        store2.quantityProductsInStock = 20;

        given()
                .when().get("/store")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldReturnSingleStore() {
        Store store = new Store();
        store.id = 1L;
        store.name = "Store-A";
        store.quantityProductsInStock = 50;

        when(storeService.getById(1L)).thenReturn(store);

        given()
                .when().get("/store/1")
                .then()
                .statusCode(200)
                .body("name", is("Store-A"))
                .body("quantityProductsInStock", is(50));
    }

    @Test
    void shouldReturn404WhenStoreNotFound() {
        when(storeService.getById(99L)).thenReturn(null);

        given()
                .when().get("/store/99")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldCreateStoreSuccessfully() {
        Store store = new Store();
        store.name = "New-Store";
        store.quantityProductsInStock = 30;

        when(storeService.create(any(Store.class))).thenReturn(store);

        given()
                .contentType(ContentType.JSON)
                .body(store)
                .when().post("/store")
                .then()
                .statusCode(201);
    }


    @Test
    void shouldFailCreateWhenIdIsPresent() {
        Store store = new Store();
        store.id = 10L;
        store.name = "Invalid-Store";

        given()
                .contentType(ContentType.JSON)
                .body(store)
                .when().post("/store")
                .then()
                .statusCode(422);
    }

    @Test
    void shouldUpdateStoreSuccessfully() {
        Store existing = new Store();
        existing.id = 1L;
        existing.name = "Old";
        existing.quantityProductsInStock = 5;

        Store updated = new Store();
        updated.name = "Updated";
        updated.quantityProductsInStock = 25;

        when(storeService.getById(1L)).thenReturn(existing);
        when(storeService.update(any(Store.class))).thenAnswer(invocation -> {
            Store arg = invocation.getArgument(0);
            arg.id = 1L;
            return arg;
        });

        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when().put("/store/1")
                .then()
                .statusCode(200)
                .body("name", is("Updated"))
                .body("quantityProductsInStock", is(25));
    }

    @Test
    void shouldFailUpdateWhenStoreNotFound() {
        Store updated = new Store();
        updated.name = "Updated";

        when(storeService.getById(5L)).thenReturn(null);

        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when().put("/store/5")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldPatchStoreSuccessfully() {
        Store existing = new Store();
        existing.id = 2L;
        existing.name = "Store-X";
        existing.quantityProductsInStock = 10;

        Store patch = new Store();
        patch.name = "Store-Y";
        patch.quantityProductsInStock = 99;

        when(storeService.getById(2L)).thenReturn(existing);
        when(storeService.patch(any(Store.class))).thenAnswer(invocation -> {
            Store arg = invocation.getArgument(0);
            arg.id = 2L;
            arg.name = "Store-Y";
            arg.quantityProductsInStock = 99;
            return arg;
        });

        given()
                .contentType(ContentType.JSON)
                .body(patch)
                .when().patch("/store/2")
                .then()
                .statusCode(200)
                .body("name", is("Store-Y"))
                .body("quantityProductsInStock", is(99));
    }


    @Test
    void shouldDeleteStoreSuccessfully() {
        Store existing = new Store();
        existing.id = 3L;

        when(storeService.getById(3L)).thenReturn(existing);
        doNothing().when(storeService).delete(existing);

        given()
                .when().delete("/store/3")
                .then()
                .statusCode(204);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingStore() {
        when(storeService.getById(7L)).thenReturn(null);

        given()
                .when().delete("/store/7")
                .then()
                .statusCode(404);
    }
}
