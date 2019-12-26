package com.SimpleFinApp.myapplication.Purchases;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SimpleFinApp.myapplication.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


// This class contains methods for the Daily Statistics page.

public class CalendarDailyActivity extends AppCompatActivity {
    private static final String TAG = "CalendarDailyActivity";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendardaily);
        setTitle(R.string.dailyCalendarHeader);
        CalendarView cv = findViewById(R.id.cvDaily);
        cv.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                initArrays(year,month,dayOfMonth);
                updateDailyTotal();
            }//met
        });
    }
    private ArrayList<Double> amounts = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> memos = new ArrayList<>();

    private void resetArrays(){
        amounts.clear();
        dates.clear();
        categories.clear();
        memos.clear();
    }

    private void initArrays(int year, int month, int dayOfMonth){
        Log.d(TAG, "initArrays: called");
        PurchaseHelper helper = new PurchaseHelper(this);
        Date date = new Date(year, month, dayOfMonth);
        Cursor results = helper.getDayOfPurchasesFrom(date);
        resetArrays();
        while (results.moveToNext()){ // customer =
            dates.add(results.getString(1));
            amounts.add(results.getDouble(3));
            categories.add(results.getString(4));
            memos.add(results.getString(5));
        }
        results.close();
        // To show the list from newest - oldest, not too efficient, better off finding a way to store it in reverse?
        Collections.reverse(amounts);
        Collections.reverse(dates);
        Collections.reverse(categories);
        Collections.reverse(memos);
        initRecyclerView(); // puts it in
    }
    private void updateDailyTotal(){
        Log.d(TAG, "updateWeeklyTotal: Updating daily total");
        TextView dailyTotal = findViewById(R.id.dailyTotal);
        double total = 0;
        for (int i=0;i<amounts.size();i++){
            double amount = amounts.get(i);
            total += amount;
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        dailyTotal.setText("Spending this day: " +  formatter.format(total));
    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.dailyRecycler);
        PurchaseRecyclerViewAdapter adapater = new PurchaseRecyclerViewAdapter(this, dates, amounts, categories, memos);
        recyclerView.setAdapter(adapater);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
    }
}
