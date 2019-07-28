package com.softsolstudio.superfarming.models;

public class categoryModel {
    private String type;
    private int icon;

    public categoryModel(String type, int icon) {
        this.type = type;
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
