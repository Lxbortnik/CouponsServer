package com.alex.coupons.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "purchases")

public class PurchaseEntity {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private CustomerEntity customer;
    @ManyToOne
    private CouponEntity coupon;
    @Column(name = "amount", nullable = false)
    private Integer amount;
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;


    public PurchaseEntity() {
    }

    public PurchaseEntity(int customerId, int couponId, int amount, Date timestamp) {
        this.customer = new CustomerEntity();
        this.customer.setId(customerId);
        this.coupon = new CouponEntity();
        this.coupon.setId(couponId);
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public PurchaseEntity(int id, int customerId, int couponId, int amount, Date timestamp) {
        this.id = id;
        this.customer = new CustomerEntity();
        this.customer.setId(customerId);
        this.coupon = new CouponEntity();
        this.coupon.setId(couponId);
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
