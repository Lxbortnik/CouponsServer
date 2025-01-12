package com.alex.coupons.dto;

public class Category {

    private int id;
    private String category;


    public Category() {
    }

    public Category(String category) {
        this.category = category;
    }

    public Category(int id, String category) {
        this(category);
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", category='" + category + '\'' +
                '}';
    }
}
