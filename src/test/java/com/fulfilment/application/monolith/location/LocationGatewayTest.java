package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.exception.InvalidInputException;
import com.fulfilment.application.monolith.exception.LocationNotFound;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class LocationGatewayTest {

  LocationGateway locationGateway = new LocationGateway();
  String searchIdentifier = null;

  @Test
  public void testWhenResolveExistingLocationShouldReturn() {
    // given
    locationGateway = new LocationGateway();
    searchIdentifier = "ZWOLLE-001";

    // when
    Location location = locationGateway.resolveByIdentifier(searchIdentifier);

    // then
    assertEquals(location.identification, searchIdentifier);
  }

  @Test
  public void testWhenInputIsEmptyOrNull() {
    // given
    locationGateway = new LocationGateway();
    searchIdentifier = null;

    // when
    InvalidInputException exception = assertThrows(InvalidInputException.class, () -> locationGateway.resolveByIdentifier(searchIdentifier));

    // then
    assertEquals(exception.getMessage(), "Identifier can not be blank.");
  }

  @Test
  public void testWhenIdentifierIsNotAvailable() {
    // given
    locationGateway = new LocationGateway();
    searchIdentifier = "IKEA_1";

    // when
    LocationNotFound exception = assertThrows(LocationNotFound.class, () -> locationGateway.resolveByIdentifier(searchIdentifier));

    // then
    assertEquals(exception.getMessage(), "Location with identifier " + searchIdentifier + " not found.");
  }
}
