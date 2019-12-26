package com.SimpleFinApp.myapplication.Purchases;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.SimpleFinApp.myapplication.R;

import java.text.NumberFormat;
import java.util.Date;


// This class is in charge of logging a new pu

public class LogActivity extends AppCompatActivity {
    private static final String TAG = "LogActivity";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logpurchase);
        setTitle(R.string.logPurchaseHeader);
    }
    public void logPurchase(View view) {
        EditText purchaseAmtField = (EditText) findViewById(R.id.purchaseAmt);
        Double amount;
        if (!(purchaseAmtField.getText().toString().equals(""))) {
            amount = Double.parseDouble(purchaseAmtField.getText().toString()); // gets the purchase amount value
        }
        else{
            amount = 0.0;
        }
        Spinner mySpinner = (Spinner) findViewById(R.id.category);
        String category = mySpinner.getSelectedItem().toString(); // gets the category selected
        EditText memoField = (EditText) findViewById(R.id.memo);
        Purchase newPurchase;
        String memo = memoField.getText().toString();
        Date date = new Date();
        Log.d(TAG, "Date found: " + date.toString());
        newPurchase = new Purchase(date, amount, category, memo); // ORDER IS DATE -> AMT -> CAT -> MEMO
        PurchaseHelper helper = new PurchaseHelper(this);
        helper.addPurchase(newPurchase);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        Log.d(TAG, "logPurchase: Formatting currency into " + formatter.format(amount));
        Toast.makeText(this, "Logged a " + formatter.format(amount)+ " for " + category, Toast.LENGTH_SHORT).show();
    }

}
