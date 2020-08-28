package com.softsolstudio.farmertrader.models;

public class SubCategoryModel {
    private String category;
    private String subCategory;
    private int icon;

    public SubCategoryModel(String category, String subCategory, int icon) {
        this.category = category;
        this.subCategory = subCategory;
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
