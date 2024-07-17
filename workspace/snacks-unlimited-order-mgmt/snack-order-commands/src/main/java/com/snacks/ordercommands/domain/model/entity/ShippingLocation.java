package com.snacks.ordercommands.domain.model.entity;

import com.snacks.ordercommands.domain.model.value.GeoLocation;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SHIPPING_LOCATION")
public class ShippingLocation {

    @Id
    private String orderId;
    private String customerName;
    private String customerAddress;
    private String zipCode;

    @Embedded
    private GeoLocation geoLocation;

    public ShippingLocation() {
    }

    public ShippingLocation(String orderId, String customerName, String customerAddress, String zipCode, GeoLocation geoLocation) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.zipCode = zipCode;
        this.geoLocation = geoLocation;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

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
}
