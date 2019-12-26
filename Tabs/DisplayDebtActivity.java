package com.SimpleFinApp.myapplication.Tabs;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SimpleFinApp.myapplication.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

// This class contains methods which are used to display an individual person's debt under the Tabs section. make it so that there can be negative input of a debt, (like u owe them)

public class DisplayDebtActivity extends AppCompatActivity {
    private static final String TAG = "DisplayDebtActivity";
    private String tabName = "default";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: started.");
        Bundle b = getIntent().getExtras();
        setContentView(R.layout.activity_displaytabs);
        if (b != null) {

            setTitle(b.getString("tabName") + "'s tab");
            tabName = b.getString("tabName"); // should've been set
        }
        initArrays();
    }
    private ArrayList<Double> amounts = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> memos = new ArrayList<>();
    public void initArrays(){
        TabsHelper helper = new TabsHelper(this);
        Cursor results = helper.returnTabOf(tabName);
        resetArrays();
        while (results.moveToNext()){ // customer =
            dates.add(results.getString(1));
            amounts.add(results.getDouble(2));
            memos.add(results.getString(4));
            Log.d(TAG, "initArrays: DATE: " + results.getString(1) + "    AMOUNT: " + results.getString(2) + "     MEMO: " + results.getString(4));
        }
        results.close();
        TextView totalTab = findViewById(R.id.totalTab);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        totalTab.setText("Total tab: " + formatter.format(helper.getTotalTabOf(tabName)));
        Collections.reverse(amounts);
        Collections.reverse(dates);
        Collections.reverse(memos);
        initRecyclerView(); // puts it in
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.tabListing);
        DebtDisplayRecyclerViewAdapter adapater = new DebtDisplayRecyclerViewAdapter(this, tabName, dates, amounts, memos);
        recyclerView.setAdapter(adapater);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
    }
    public void addToTabButton(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.addtoTabTitle);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        // First input
        final EditText amountBox = new EditText(this);
        amountBox.setHint(R.string.debt_amount);
        amountBox.setInputType(InputType.TYPE_CLASS_NUMBER |  InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(amountBox); // Notice this is an add method

        // Second input
        final EditText memoBox = new EditText(this);
        memoBox.setHint(R.string.debtMemo);
        layout.addView(memoBox); // Another add method
        dialog.setView(layout); // Again this is a set method, not add
        // add buttons
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!(amountBox.getText().toString().isEmpty() || memoBox.getText().toString().isEmpty())) {
                    DebtEntry tab = new DebtEntry(new Date(), tabName, Double.parseDouble(amountBox.getText().toString()), memoBox.getText().toString());
                    addToTab(tab);
                    initArrays();
                }
                else{
                    Toast.makeText(DisplayDebtActivity.this,"You did not complete all the requirements!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }


    private void resetArrays(){
        dates.clear();
        amounts.clear();
        memos.clear();
    }

    private void addToTab(DebtEntry tab){ // helper method to call the real addToTab
        TabsHelper helper = new TabsHelper(this);
        helper.addToTab(tab);
    }

}
