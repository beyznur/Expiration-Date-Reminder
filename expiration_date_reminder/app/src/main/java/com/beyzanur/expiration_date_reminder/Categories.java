package com.beyzanur.expiration_date_reminder;

public class Categories {

    private String Categories;
    private int Icon;

    public Categories(){}

    public Categories(String categories, int icon) {
        Categories = categories;
        Icon = icon;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }
}


