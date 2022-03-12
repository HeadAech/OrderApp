package com.example.orderapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Accessory {
    private int id;
    private String name;
    private String description;
    private String purpose;
    private float cena;
    private int qty;
    private String imageBase64;
    private Bitmap imageBm;

    public Accessory(int id, String name, String purpose, String description, float cena, int qty, String imageBase64, Bitmap imageBm) {
        this.id = id;
        this.name = name;
        this.purpose = purpose;
        this.description = description;
        this.cena = cena;
        this.qty = qty;
        this.imageBase64 = imageBase64;
        this.imageBm = imageBm;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Accessory{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", purpose='" + purpose + '\'' +
                ", cena=" + cena +
                ", qty=" + qty +
                ", imageBase64='" + "za dlugie rip" + '\'' +
                ", imageBm=" + imageBm +
                '}';
    }

    public String getName() {
        return name;
    }

    public float getCena() {
        return cena;
    }

    public int getQty() {
        return qty;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public int getId() {
        return id;
    }

    public Bitmap getImageBm() {
        return imageBm;
    }

}
