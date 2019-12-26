package com.SimpleFinApp.myapplication.Purchases;

import android.content.Intent;
import android.os.Bundle;

import com.SimpleFinApp.myapplication.R;
import com.SimpleFinApp.myapplication.Tabs.TabsBrancherActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

// This is the home screen which is a brancher to options regarding purchases.

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_purchases:
                    return true;
                case R.id.navigation_tabs:
                    Intent intent = new Intent(MainActivity.this, TabsBrancherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.purchases);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void goToLogPurchase(View view){
        Intent intent = new Intent(MainActivity.this, LogActivity.class);
        startActivity(intent);
    }

    public void goToViewPurchases(View view){
        Intent intent = new Intent(MainActivity.this, DisplayPurchasesActivity.class);
        startActivity(intent);
    }
    public void goToStatistics(View view){
        Intent intent = new Intent(MainActivity.this, CalendarBrancherActivity.class);
        startActivity(intent);
    }

    public void populatePurchases(View view) {
       // public Purchase(Date dateOfPurchase, double amount, String category, String memo){
        PurchaseHelper helper = new PurchaseHelper(this);
        long randDateAsLong;
        double randAmt;
        String randCategory;
        String[] categories = new String[]{
                "Groceries, food and drink",
                "Apparel",
                "Health and beauty",
                "Household products",
                "Technology",
                "Other"
        };
        int numOfRandEntries = 100;
        for (int i=0;i<numOfRandEntries;i++){
             randDateAsLong = (long) (Math.random() * 31449600000L) + 1546318800000L; // RANDOM NUMBER BETWEEN JAN 1, 2019 -> DEC 31 2019
             randAmt = Math.random() * 1000; // random amt between 0-1000
             randCategory = getRandom(categories);
             helper.addPurchase(new Purchase(new Date(randDateAsLong),randAmt,randCategory,"testing entry " + i));
        }
        Toast.makeText(this, numOfRandEntries + " random entries added.", Toast.LENGTH_SHORT).show();
    }
    public String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

}
