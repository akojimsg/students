package com.codechallenge.model;

import java.io.Serializable;


public class User implements Serializable {
    private int id;
    private String userName;
    private String email;
    private String phone;
    private String website;

    public User(int id, String userName, String email, String phone, String website) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.website = website;
    }

    public int getId() {
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
