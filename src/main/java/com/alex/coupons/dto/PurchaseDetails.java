package com.alex.coupons.dto;
import java.sql.Timestamp;
import java.util.Date;

public class PurchaseDetails {

    private Integer id;
    private Integer couponId;
    private int amount;
    private Integer customerId;
    private Date timestamp;
    private float totalPrice;
    private String title;
    private String description;
    private Date endDate;


    public PurchaseDetails() {
    }

    public PurchaseDetails(Integer couponId, int amount, Integer customerId, Date timestamp, float totalPrice, String title, String description, Date endDate) {
        this.couponId = couponId;
        this.amount = amount;
        this.customerId = customerId;
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
        this.title = title;
        this.description = description;
        this.endDate = endDate;
    }

    public PurchaseDetails(Integer id, Integer couponId, int amount, Integer customerId, Date timestamp, float totalPrice, String title, String description, Date endDate) {
        this.id = id;
        this.couponId = couponId;
        this.amount = amount;
        this.customerId = customerId;
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
        this.title = title;
        this.description = description;
        this.endDate =endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "PurchaseDetails{" +
                "id=" + id +
                ", couponId=" + couponId +
                ", amount=" + amount +
                ", customerId=" + customerId +
                ", timestamp=" + timestamp +
                ", totalPrice=" + totalPrice +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", endDate=" + endDate +
                '}';
    }
}
