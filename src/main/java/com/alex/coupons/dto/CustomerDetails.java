package com.alex.coupons.dto;

public class CustomerDetails {
    private int id;
    private String name;
    private String username;
    private String address;
    private String phone;

    public CustomerDetails() {

    }

    public CustomerDetails(String name, String username, String address, String phone) {
        this.name = name;
        this.username = username;
        this.address = address;
        this.phone = phone;
    }

    public CustomerDetails(int id, String name, String username, String address, String phone) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CustomerDetails{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
