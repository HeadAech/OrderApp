package com.example.orderapp;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Serializable {
    private int id;
    private ArrayList<Integer> pcIds;
    private ArrayList<Integer> accIds;
    private LocalDateTime date;
    private Double price;
    private String companyName;

    public Order(int id, ArrayList<Integer> pcIds, ArrayList<Integer> accIds, LocalDateTime date, Double price, String companyName){
        this.id = id;
        this.pcIds = pcIds;
        this.accIds = accIds;
        this.date = date;
        this.price = price;
        this.companyName = companyName;
    }

    public int getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String toString() {
        return "Order{" +
                "pcIds=" + pcIds +
                ", accIds=" + accIds +
                ", date=" + date +
                ", price=" + price +
                '}';
    }

    public LocalDateTime getDate() {
        return date;
    }

    public ArrayList<Integer> getPcIds() {
        return pcIds;
    }

    public ArrayList<Integer> getAccIds() {
        return accIds;
    }

    public Double getPrice() {
        return price;
    }
}
