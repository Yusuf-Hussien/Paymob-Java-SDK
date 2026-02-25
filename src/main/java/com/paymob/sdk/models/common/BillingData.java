package com.paymob.sdk.models.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Customer billing information used for payment processing.
 */
public class BillingData {
    private String apartment;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("street")
    private String street;

    @JsonProperty("building")
    private String building;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("city")
    private String city;

    @JsonProperty("country")
    private String country;

    @JsonProperty("email")
    private String email;

    @JsonProperty("floor")
    private String floor;

    private String state;

    public BillingData() {
    }

    // Required fields: firstName, lastName, email, phoneNumber
    public BillingData(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and setters
    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static class Builder {
        private String apartment;
        private String firstName;
        private String lastName;
        private String street;
        private String building;
        private String phoneNumber;
        private String city;
        private String country;
        private String email;
        private String floor;
        private String state;

        public Builder apartment(String apartment) {
            this.apartment = apartment;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder building(String building) {
            this.building = building;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder floor(String floor) {
            this.floor = floor;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public BillingData build() {
            BillingData billingData = new BillingData(firstName, lastName, email, phoneNumber);
            billingData.setApartment(apartment);
            billingData.setStreet(street);
            billingData.setBuilding(building);
            billingData.setCity(city);
            billingData.setCountry(country);
            billingData.setFloor(floor);
            billingData.setState(state);
            return billingData;
        }
    }
}
