package com.httpapp.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;


public class User implements Serializable, Comparable {
    private String id;
    private String name;
    private String userName;
    private String email;

    public Address getAddress() {
        return address;
    }

    private Address address;
    private String phone;
    private String website;
    private Map<Object, Object> company;

    public Map<Object, Object> getCompany() {
        return company;
    }

    public User(String id,
                String name,
                String userName,
                String email,
                Address address,
                String phone,
                String website,
                Map company) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + getName() + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address.toString() + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                ", company='" + company + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        User user = (User) o;
        int comparison = user.name.compareTo(name);
        if (comparison > 0) {
            return -1;
        } else if (comparison == 0) {
            return Double.valueOf(user.id) - Double.valueOf(id) > 0 ? 1 :
                    Double.valueOf(user.id) - Double.valueOf(id) < 0 ? -1 : 0;
        } else return 1;
    }
}
