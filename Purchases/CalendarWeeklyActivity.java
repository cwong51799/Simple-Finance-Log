package com.SimpleFinApp.myapplication.Purchases;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SimpleFinApp.myapplication.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


// This class contains methods for the weekly statistics page.

public class CalendarWeeklyActivity extends AppCompatActivity {
    private static final String TAG = "StatisticsWeeklyActivit";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarweekly);
        setTitle(R.string.weeklyCalendarHeader);
        CalendarView cv = (CalendarView)findViewById(R.id.cvWeekly);
        cv.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                Log.d(TAG, "onSelectedDayChange: " + dayOfWeek);
                if (dayOfWeek == 1){
                    initArrays(year,month,dayOfMonth);
                    updateWeeklyTotal();
                }
                else{
                    Toast.makeText(CalendarWeeklyActivity.this, R.string.sundayOnlyToast, Toast.LENGTH_SHORT).show();
            }
            }
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
        PurchaseHelper helper = new PurchaseHelper(this);
        Date date = new Date(year, month, dayOfMonth);
        Log.d(TAG, "initArrays: String found: " + date.getYear());
        Cursor results = helper.getWeekOfPurchasesFrom(date);
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
    private void updateWeeklyTotal(){
        Log.d(TAG, "updateWeeklyTotal: Updating weekly total");
        TextView weeklyTotal = (TextView) findViewById(R.id.weeklyTotal);
        double total = 0;
        for (int i=0;i<amounts.size();i++){
            double amount = amounts.get(i);
            total += amount;
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        weeklyTotal.setText("Spending this week: "+ formatter.format(total));
    }
    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.weeklyRecycler);
        PurchaseRecyclerViewAdapter adapater = new PurchaseRecyclerViewAdapter(this, dates, amounts, categories, memos);
        recyclerView.setAdapter(adapater);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
    }

}
