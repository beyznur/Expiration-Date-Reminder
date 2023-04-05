package com.beyzanur.expiration_date_reminder;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Products {
    String productName;
    String productCat;
    String productDate;
    String productNotification;


    public Products(String productName, String productCat, String productDate, String productNotification) {
        this.productName = productName;
        this.productCat = productCat;
        this.productDate = productDate;
        this.productNotification = productNotification;
    }

    public Products(String productName, String productCat) {
        this.productName = productName;
        this.productCat = productCat;
    }

    public Products(){}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCat() {
        return productCat;
    }

    public void setProductCat(String productCat) {
        this.productCat = productCat;
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public String getProductNotification() {
        return productNotification;
    }

    public void setProductNotification(String productNotification) {
        this.productNotification = productNotification;
    }



}
