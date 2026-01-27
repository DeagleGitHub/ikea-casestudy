package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class LocationGatewayTest {

  @DisplayName("GIVEN existing identifiers WHEN retrieving locations THEN should return correct location details")
  @ParameterizedTest(name = "Checking identifier {0}")
  @CsvSource({
      "ZWOLLE-001, 1, 40",
      "ZWOLLE-002, 2, 50",
      "AMSTERDAM-001, 5, 100",
      "AMSTERDAM-002, 3, 75",
      "TILBURG-001, 1, 40",
      "HELMOND-001, 1, 45",
      "EINDHOVEN-001, 2, 70",
      "VETSBY-001, 1, 90"
  })
  void whenResolveExistingLocation_ShouldReturnLocationDetails(String identifier, int warehouses, int capacity) {
    LocationGateway locationGateway = new LocationGateway();

    Location location = locationGateway.resolveByIdentifier(identifier);

    assertEquals(identifier, location.identification);
    assertEquals(warehouses, location.maxNumberOfWarehouses);
    assertEquals(capacity, location.maxCapacity);
  }

  @DisplayName("GIVEN invalid or null identifiers WHEN resolving locations THEN should return null")
  @ParameterizedTest(name = "Checking invalid identifier: {0}")
  @CsvSource(value = {
      "NULL",
      "NON-EXISTING",
      "''",
      "' '"
  }, nullValues = "NULL")
  void whenResolveInvalidIdentifier_ShouldReturnNull(String identifier) {
    LocationGateway locationGateway = new LocationGateway();

    Location location = locationGateway.resolveByIdentifier(identifier);

    assertNull(location, "Should return null for invalid input: " + identifier);
  }
}