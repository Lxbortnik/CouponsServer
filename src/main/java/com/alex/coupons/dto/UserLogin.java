package com.alex.coupons.dto;

//@Component // to check why
public class UserLogin {
    private Integer id;
    private String userType;
    private Integer companyId;

    public UserLogin() {    }

    public UserLogin(Integer id, String userType, Integer companyId) {
        this.id = id;
        this.userType = userType;
        this.companyId = companyId;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getUserType() {return userType;}
    public void setUserType(String userType) {this.userType = userType;}

    public Integer getCompanyId() {return companyId;}
    public void setCompanyId(Integer companyId) {this.companyId = companyId;}


}


