package com.alex.coupons.dto;

import java.sql.Timestamp;
import java.util.Date;

public class Coupon {

    private int id;
    private String title;
    private String description;
    private float price;
    private Integer companyId;
    private Integer categoryId;
    private Date startDate;
    private Date endDate;
    private int amount;
    private String imageUrl;


    public Coupon() {
    }

    public Coupon(String title, String description, float price, Integer companyId, Integer categoryId, Date startDate, Date endDate, int amount, String imageUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.companyId = companyId;
        this.categoryId = categoryId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.imageUrl = imageUrl;

    }

    public Coupon(int id, String title, String description, float price, Integer companyId, Integer categoryId, Date startDate, Date endDate, int amount, String imageUrl) {
        this(title, description, price, companyId, categoryId, startDate, endDate, amount, imageUrl);
        this.id = id;
    }

    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setPrice(float price) {this.price = price;}
    public void setCompanyId(Integer companyId) {this.companyId = companyId;}
    public void setCategoryId(Integer categoryId) {this.categoryId = categoryId;}
    public void setStartDate(Date startDate) {this.startDate = startDate;}
    public void setEndDate(Date endDate) {this.endDate = endDate;}
    public void setAmount(int amount) {this.amount = amount;}
    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public int getId() {return id;}
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public float getPrice() {return price;}
    public Integer getCompanyId() {return companyId;}
    public Integer getCategoryId() {return categoryId;}
    public Date getStartDate() {return startDate;}
    public Date getEndDate() {return endDate;}
    public int getAmount() {return amount;}
    public String getImageUrl() {return imageUrl;}

    public void setId(int id) {
        this.id = id;
    }

    // why I needed to change date to timestamp, even if I do not need specific hour?

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", companyId=" + companyId +
                ", categoryId=" + categoryId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amount=" + amount +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
