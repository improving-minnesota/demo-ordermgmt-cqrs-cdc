package com.snacks.orderprocessor.model.domain;

import com.snacks.orderprocessor.model.domain.value.GeoLocation;

public class ShippingLocation {

    private String customerName;
    private String customerAddress;
    private String zipCode;
    private GeoLocation geoLocation;

    public ShippingLocation() {
    }

    public ShippingLocation(String customerName, String customerAddress, String zipCode, GeoLocation geoLocation) {
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.zipCode = zipCode;
        this.geoLocation = geoLocation;
    }

    // region Getters and Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    // endregion
}
