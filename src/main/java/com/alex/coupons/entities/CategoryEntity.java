package com.alex.coupons.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")

public class CategoryEntity {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "category", nullable = false)
    private String category;
    @OneToMany (mappedBy = "category", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<CouponEntity> coupons;


    // constructor
    public CategoryEntity() {
    }

    public CategoryEntity(String category) {
        this.category = category;
    }

    public CategoryEntity(int id, String category) {
        this.id = id;
        this.category = category;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.category = category;
    }

    public List<CouponEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponEntity> coupons) {
        this.coupons = coupons;
    }
}
