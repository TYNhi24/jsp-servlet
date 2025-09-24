package com.multilang.model;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private BigDecimal price;
    private BigDecimal weight;
    private String productName;
    private String productDescription;
    private ProductCategory productCategory;

    // Constructors
    public Product() {}

    public Product(int productId, BigDecimal price, BigDecimal weight, String productName, 
                   String productDescription, ProductCategory productCategory) {
        this.productId = productId;
        this.price = price;
        this.weight = weight;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", price=" + price +
                ", weight=" + weight +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productCategory=" + productCategory +
                '}';
    }
}