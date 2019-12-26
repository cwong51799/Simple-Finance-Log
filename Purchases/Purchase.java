package com.SimpleFinApp.myapplication.Purchases;

import java.text.DecimalFormat;
import java.util.Date;

// This class represents a purchase to be logged.

public class Purchase {
    public Date dateOfPurchase;
    public double amount;
    public String category;
    public String memo;
    public Purchase(Date dateOfPurchase, double amount, String category, String memo){
        this.dateOfPurchase = dateOfPurchase;
        DecimalFormat df = new DecimalFormat("#.##");
        this.amount = Double.parseDouble(df.format(amount));;
        this.category = category;
        this.memo = memo;
    }
    public String getMemo() {
        return memo;
    }
    public long getDateAsLong(){
        return dateOfPurchase.getTime();
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public Date getDateOfPurchase() { // returns string version
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
