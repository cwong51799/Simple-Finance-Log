package com.SimpleFinApp.myapplication.Tabs;

import java.text.DecimalFormat;
import java.util.Date;

public class DebtEntry {
    public Date dateDebtAquired;
    public String whoOwesMe;
    public double amount;
    public String memo;
    private static final String TAG = "DebtEntry";
    public DebtEntry(Date dateDebtAquired, String whoOwesMe, double amount, String memo) {
        this.dateDebtAquired = dateDebtAquired;
        this.whoOwesMe = whoOwesMe;
        DecimalFormat df = new DecimalFormat("#.##");
        this.amount = Double.parseDouble(df.format(amount));;
        this.memo = memo;
    }

    public Date getDateDebtAquired() {
        return dateDebtAquired;
    }

    public void setDateDebtAquired(Date dateDebtAquired) {
        this.dateDebtAquired = dateDebtAquired;
    }

    public String getWhoOwesMe() {
        return whoOwesMe;
    }
    public void setWhoOwesMe(String whoOwesMe) {
        this.whoOwesMe = whoOwesMe;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
