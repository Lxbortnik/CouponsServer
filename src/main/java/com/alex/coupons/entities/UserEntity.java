package com.alex.coupons.entities;

import javax.persistence.*;

@Entity
@Table(name = "users")

public class UserEntity {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "username", unique = true, nullable = false, length = 45)
    private String username;
    @Column(name = "password", nullable = false, length = 45)
    private String password;
    @Column(name = "user_type", nullable = false, length = 45)
    private String userType;
    @ManyToOne
    private CompanyEntity company;


    public UserEntity() {
    }

    public UserEntity(String username, String password, String userType, Integer companyId) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        if (userType.equals("company")) {
            this.company = new CompanyEntity();
            this.company.setId(companyId);
        }
    }

    public UserEntity(int id, String username, String password, String userType, Integer companyId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
        if (userType.equals("company")) {
            this.company = new CompanyEntity();
            this.company.setId(companyId);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }
}
