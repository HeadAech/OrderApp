package com.example.orderapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Zestaw {
    private int id;
    private String name;
    private String description;
    private float price;
    private int qty;
    private String zdjecieBase64;
    private Bitmap imageBM;


    public Bitmap getImageBM() {
        return imageBM;
    }

    public int getId() {
        return id;
    }

    public Zestaw(int id, String name, String description, float price, int qty, String zdjecieBase64, Bitmap imageBM){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.qty = qty;
        this.zdjecieBase64 = zdjecieBase64;
        this.imageBM = imageBM;
    }

    @Override
    public String toString() {
        return "Zestaw{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", zdjecieBase64='" + "mega dlugi string" + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public String getZdjecieBase64() {
        return zdjecieBase64;
    }

}
