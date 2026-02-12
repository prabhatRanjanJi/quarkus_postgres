package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.exception.InvalidInputException;
import com.fulfilment.application.monolith.exception.LocationNotFound;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LocationGateway implements LocationResolver {

  private static final List<Location> locations = new ArrayList<>();

  static {
    locations.add(new Location("ZWOLLE-001", 1, 40));
    locations.add(new Location("ZWOLLE-002", 2, 50));
    locations.add(new Location("AMSTERDAM-001", 5, 100));
    locations.add(new Location("AMSTERDAM-002", 3, 75));
    locations.add(new Location("TILBURG-001", 1, 40));
    locations.add(new Location("HELMOND-001", 1, 45));
    locations.add(new Location("EINDHOVEN-001", 2, 70));
    locations.add(new Location("VETSBY-001", 1, 90));
  }

  @Override
  public Location resolveByIdentifier(String identifier) {
    if (StringUtils.isNotBlank(identifier)) {
      return locations.stream().filter(locationObj -> StringUtils.equals(locationObj.identification, identifier))
              .findFirst().orElseThrow(() -> new LocationNotFound("Location with identifier " + identifier + " not found."));
    } else {
      throw new InvalidInputException("Identifier can not be blank.");
    }
  }
}
