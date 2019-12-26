package com.SimpleFinApp.myapplication.Purchases;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.SimpleFinApp.myapplication.R;

// This class branches between Daily and Weekly statistics

public class CalendarBrancherActivity extends AppCompatActivity {
    PurchaseHelper helper = new PurchaseHelper(this);
    private static final String TAG = "CalendarBrancherActivity";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarbrancher);
        setTitle(R.string.viewCalendarHeader);
    }

    public void goToDailyStats(View view){
        Intent intent = new Intent(this, CalendarDailyActivity.class);
        startActivity(intent);
    }
    public void goToWeeklyStats(View view){
        Intent intent = new Intent(this, CalendarWeeklyActivity.class);
        startActivity(intent);
    }
/*    public boolean goToTotalStats(View view){  < --- Total stats useless or na?
        setContentView(R.layout.activity_statstotal);
        setTitle(R.string.totalPurchasesHeader);
        if (initTotalStatFields(findViewById(R.id.statPage)) == -1){
            Toast.makeText(this, R.string.noLoggedPurchasesToast, Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_calendarbrancher);
            return true;
        }
        return false;
    }
    private int initTotalStatFields(View view) {
        TextView totalSpent = findViewById(R.id.totalSpent);
        String firstPurchase = helper.getFirstPurchase();
        if (firstPurchase.equals("error")) { // an exception was called
            return -1; // error
        }
        Log.d(TAG, "initTotalStatFields: " + totalSpent.getText().toString());
        totalSpent.setText("Total amount spent since " + firstPurchase + ": $" + Double.toString(helper.getTotalAmountSpent()));
        TextView totalFood = findViewById(R.id.totalFood);
        totalFood.setText("Groceries, food and drink: $" + Double.toString(helper.getTotalAmountSpentOn("Groceries, food and drink")));
        TextView totalApparel = findViewById(R.id.totalApparel);
        totalApparel.setText("Apparel: $" + Double.toString(helper.getTotalAmountSpentOn("Apparel")));
        TextView totalBeauty = findViewById(R.id.totalBeauty);
        totalBeauty.setText("Health and beauty: $" + Double.toString(helper.getTotalAmountSpentOn("Health and beauty")));
        TextView totalHouseholdProduct = findViewById(R.id.totalHouseholdProduct);
        totalHouseholdProduct.setText("Household products: $" + Double.toString(helper.getTotalAmountSpentOn("Household products")));
        TextView totalGames = findViewById(R.id.totalGames);
        totalGames.setText("Technology: $" + Double.toString(helper.getTotalAmountSpentOn("Games")));
        TextView totalOther = findViewById(R.id.totalOther);
        totalOther.setText("Other: $" + Double.toString(helper.getTotalAmountSpentOn("Other")));
        return 0;
    }*/
}
