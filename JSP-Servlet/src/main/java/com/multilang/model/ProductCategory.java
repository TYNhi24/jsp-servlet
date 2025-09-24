package com.multilang.model;

public class ProductCategory {
    private int productCategoryId;
    private boolean canBeShipped;
    private String categoryName; // From translation table

    // Constructors
    public ProductCategory() {}

    public ProductCategory(int productCategoryId, boolean canBeShipped, String categoryName) {
        this.productCategoryId = productCategoryId;
        this.canBeShipped = canBeShipped;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public boolean isCanBeShipped() {
        return canBeShipped;
    }

    public void setCanBeShipped(boolean canBeShipped) {
        this.canBeShipped = canBeShipped;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "ProductCategory{" +
                "productCategoryId=" + productCategoryId +
                ", canBeShipped=" + canBeShipped +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}