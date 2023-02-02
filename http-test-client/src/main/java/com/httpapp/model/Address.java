package com.httpapp.model;

import java.io.Serializable;
import java.util.Map;

public class Address implements Serializable {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Map<String, String> geo;

    public Address(String street, String suite, String city, String zipcode, Map<String, String> geo) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.geo = geo;
    }

    public static Address fromMap(Map<String, Object> map) {
        Address address = new Address(
                map.get("street").toString(),
                map.get("suite").toString(),
                map.get("city").toString(),
                map.get("zipcode").toString(),
                (Map<String, String>) map.get("geo"));
        return address;
    }

    public String getStreet() {
        return street;
    }

    public String getSuite() {
        return suite;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public Map<String, String> getGeo() {
        return geo;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", suite='" + suite + '\'' +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", geo=" + geo +
                '}';
    }
}
