package com.fulfilment.application.monolith.warehousefulfilment.adapters.restapi;

import com.fulfilment.application.monolith.warehousefulfilment.domain.model.WarehouseFulfilment;
import com.fulfilment.application.monolith.warehousefulfilment.domain.usecases.WarehouseFulfilmentUseCase;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@QuarkusTest
public class WarehouseFulfilmentResourceTest {

    @InjectMock
    WarehouseFulfilmentUseCase warehouseFulfilmentUseCase;

    @Test
    void shouldCreateWarehouseFulfilmentSuccessfully() {
        WarehouseFulfilment fulfilment = new WarehouseFulfilment();
        fulfilment.setWarehouse("W1");
        fulfilment.setStore("S1");
        fulfilment.setProduct("P1");

        given()
                .contentType(ContentType.JSON)
                .body(fulfilment)
                .when()
                .post("/store/createWarehouseFulfilment")
                .then()
                .statusCode(200)
                .body("warehouse", org.hamcrest.Matchers.is("W1"))
                .body("store", org.hamcrest.Matchers.is("S1"))
                .body("product", org.hamcrest.Matchers.is("P1"));

        verify(warehouseFulfilmentUseCase).save(any(WarehouseFulfilment.class));
    }
}
