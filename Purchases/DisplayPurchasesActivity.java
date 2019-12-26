package com.SimpleFinApp.myapplication.Purchases;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SimpleFinApp.myapplication.R;

import java.util.ArrayList;
import java.util.Collections;


// This class is in charge of displaying ALL purchases under the View Purchases button.

public class DisplayPurchasesActivity extends AppCompatActivity {
    private static final String TAG = "DisplayPurchasesActivit";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaypurchases);
        setTitle(R.string.purchaseHistoryHeader);
        Log.d(TAG, "onCreate: started.");
        initArrays();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.reset_purchases_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.resetPurchasesMenuItem:
                AlertDialog.Builder resetBuilder = new AlertDialog.Builder(this);
                resetBuilder.setCancelable(true);
                resetBuilder.setTitle("Are you sure?");
                resetBuilder.setMessage("This change cannot be undone.");
                resetBuilder.setPositiveButton("I'm sure",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PurchaseHelper helper = new PurchaseHelper(DisplayPurchasesActivity.this);
                                helper.wipeData();
                                Toast.makeText(DisplayPurchasesActivity.this, "Purchases have been wiped.", Toast.LENGTH_SHORT).show();
                                // Refresh after wiping
                                Intent intent = new Intent(DisplayPurchasesActivity.this, DisplayPurchasesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // smooth, still reloading the page but smoother.
                                startActivity(intent);
                            }
                        });
                resetBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = resetBuilder.create();
                dialog.show();
                break;
            case R.id.populateMenuItem:
                AlertDialog.Builder populateBuilder = new AlertDialog.Builder(this);
                populateBuilder.setCancelable(true);
                populateBuilder.setTitle("Are you sure?");
                populateBuilder.setMessage("This change will add a LOT of random purchases. This is meant for testing purposes. ");
                populateBuilder.setPositiveButton("I'm sure",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PurchaseHelper helper = new PurchaseHelper(DisplayPurchasesActivity.this);
                                helper.populatePurchases();
                                Toast.makeText(DisplayPurchasesActivity.this, "Random purchases have been added.", Toast.LENGTH_SHORT).show();
                                // Refresh after wiping
                                Intent intent = new Intent(DisplayPurchasesActivity.this, DisplayPurchasesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // smooth, still reloading the page but smoother.
                                startActivity(intent);
                            }
                        });
                populateBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog2 = populateBuilder.create();
                dialog2.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private ArrayList<Double> amounts = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> memos = new ArrayList<>();
    private void initArrays(){
        Log.d(TAG, "initArrays: called");
        PurchaseHelper helper = new PurchaseHelper(this);
        Cursor results = helper.getAllPurchases();
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
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        PurchaseRecyclerViewAdapter adapater = new PurchaseRecyclerViewAdapter(this, dates, amounts, categories, memos);
        recyclerView.setAdapter(adapater);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
    }
}
