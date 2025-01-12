package com.alex.coupons.dto;

public class Customer {

    private int id;
    private String name;
    private String address;
    private String phone;
    private User user;


    public Customer() {
    }

    public Customer(String name, String address, String phone, User user) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.user = user;
    }

    public Customer(int id, String name, String address, String phone, User user) {
        this(name, address, phone, user);
        this.id = id;
    }


    public int getId() {return id;}
    public String getName() {return name;}
    public String getAddress() {return address;}
    public String getPhone() {return phone;}

    public User getUser() {
        return user;
    }

    public void setName(String name) {this.name = name;}
    public void setAddress(String address) {this.address = address;}
    public void setPhone(String phone) {this.phone = phone;}

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", user=" + user +
                '}';
    }
}

