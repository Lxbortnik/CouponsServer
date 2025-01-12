package com.alex.coupons.entities;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "coupons")

public class CouponEntity {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "title", nullable = false, length = 45) // מחייב וולידציה  length
    private String title;
    @Column(name = "description", nullable = false, length = 225)
    private String description;
    @Column(name = "price", nullable = false)
    private float price;
    @ManyToOne(fetch = FetchType.EAGER)
    private CategoryEntity category;
    @ManyToOne(fetch = FetchType.EAGER)
    private CompanyEntity company;
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<PurchaseEntity> purchasesEntityList;
    @Column(name = "startDate", nullable = false)
    private Date startDate;
    @Column(name = "endDate", nullable = false)
    private Date endDate;
    @Column(name = "amount", nullable = false)
    private int amount;
    @Column(name = "imageUrl", length = 254)
    private String imageUrl;


    public CouponEntity() {
    }

    public CouponEntity(String title, String description, float price, int category, int company, Date startDate, Date endDate, int amount, String imageUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = new CategoryEntity();
        this.category.setId(category);
        this.company = new CompanyEntity();
        this.company.setId(company);
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.imageUrl = imageUrl;
    }

    public CouponEntity(int id, String title, String description, float price, int category, int company, Date startDate, Date endDate, int amount, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = new CategoryEntity();
        this.category.setId(category);
        this.company = new CompanyEntity();
        this.company.setId(company);
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.imageUrl = imageUrl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    public List<PurchaseEntity> getPurchasesEntityList() {
        return purchasesEntityList;
    }

    public void setPurchasesEntityList(List<PurchaseEntity> purchasesEntityList) {
        this.purchasesEntityList = purchasesEntityList;
    }
}
