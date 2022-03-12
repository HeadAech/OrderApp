package com.example.orderapp;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable{
//    private HashMap<Integer, Zestaw> pcs = new HashMap<>();
//    private HashMap<Integer, ArrayList<Accessory>> accessories = new HashMap<>();

    private ArrayList<Integer> nrZestawuArr = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> pcIds = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> accesoryIds = new ArrayList<>();
    private ArrayList<Double> prices = new ArrayList<>();
    private int numer;

//    private int id = 0;

    public Cart(){}

    public Cart(ArrayList<ArrayList<Integer>> pcIds, ArrayList<ArrayList<Integer>> accesoryIds, ArrayList<Integer> nrZestawuArr){
        this.pcIds = pcIds;
        this.accesoryIds = accesoryIds;
        this.nrZestawuArr = nrZestawuArr;
    }


    public void addToCart(ArrayList<Integer> pcs, ArrayList<Integer> accesories, double price, int numer){
        pcIds.add(pcs);
        accesoryIds.add(accesories);
        prices.add(price);
        this.numer = numer;
    }

    public int getPcIdsLength(){
        return pcIds.size();
    }

    public ArrayList<ArrayList<Integer>> getPcIds() {
        return pcIds;
    }

    public ArrayList<ArrayList<Integer>> getAccesoryIds() {
        return accesoryIds;
    }

    public ArrayList<Double> getPrices() {
        return prices;
    }

    public int getNumer() {
        return numer;
    }

    public ArrayList<Integer> getNrZestawuArr() {
        return nrZestawuArr;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "nrZestawuArr=" + nrZestawuArr +
                ", pcIds=" + pcIds +
                ", accesoryIds=" + accesoryIds +
                ", prices=" + prices +
                '}';
    }
}
