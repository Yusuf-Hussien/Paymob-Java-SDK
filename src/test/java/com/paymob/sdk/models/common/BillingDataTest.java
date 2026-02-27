package com.paymob.sdk.models.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BillingDataTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void builder_setsAllFields() {
        BillingData data = BillingData.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.com")
                .phoneNumber("+123456")
                .city("Cairo")
                .country("EG")
                .street("Main St")
                .building("1")
                .floor("2")
                .apartment("3")
                .state("Cairo")
                .build();

        assertEquals("John", data.getFirstName());
        assertEquals("Doe", data.getLastName());
        assertEquals("john@doe.com", data.getEmail());
        assertEquals("+123456", data.getPhoneNumber());
        assertEquals("Cairo", data.getCity());
        assertEquals("EG", data.getCountry());
        assertEquals("Main St", data.getStreet());
        assertEquals("1", data.getBuilding());
        assertEquals("2", data.getFloor());
        assertEquals("3", data.getApartment());
        assertEquals("Cairo", data.getState());
    }

    @Test
    void serialization_usesSnakeCase() throws Exception {
        BillingData data = BillingData.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("0123")
                .build();

        String json = mapper.writeValueAsString(data);

        assertTrue(json.contains("\"first_name\":\"John\""));
        assertTrue(json.contains("\"last_name\":\"Doe\""));
        assertTrue(json.contains("\"phone_number\":\"0123\""));
    }

    @Test
    void deserialization_fromSnakeCase() throws Exception {
        String json = "{\"first_name\":\"Jane\",\"last_name\":\"Smith\",\"email\":\"jane@smith.com\",\"phone_number\":\"0987\"}";

        BillingData data = mapper.readValue(json, BillingData.class);

        assertEquals("Jane", data.getFirstName());
        assertEquals("Smith", data.getLastName());
        assertEquals("jane@smith.com", data.getEmail());
        assertEquals("0987", data.getPhoneNumber());
    }
}
