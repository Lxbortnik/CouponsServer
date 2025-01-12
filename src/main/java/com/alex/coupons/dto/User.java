package com.alex.coupons.dto;

public class User {

    private int id;
    private String username;
    private String password;
    private String userType;
    private Integer companyId;


    public User() {
    }

    public User(int id, String username, String userType, Integer companyId) {
        this.id = id;
        this.username = username;
        this.userType = userType;
        this.companyId = companyId;
    }

    public User(String username, String password, String userType, Integer companyId) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.companyId = companyId;
    }

    public User(int id, String username, String password, String userType, Integer companyId) {
        this(username, password, userType, companyId);
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
