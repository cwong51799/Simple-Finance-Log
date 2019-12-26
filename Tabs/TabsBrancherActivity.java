package com.SimpleFinApp.myapplication.Tabs;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SimpleFinApp.myapplication.Purchases.MainActivity;
import com.SimpleFinApp.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class TabsBrancherActivity extends AppCompatActivity {
    private static final String TAG = "TabsActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_purchases:
                    Intent intent = new Intent(TabsBrancherActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_tabs:
                    return true;
            }
            return false;
        }
    };

    private ArrayList<String> tabNames = new ArrayList<>();
    private String newTabString;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabsbrancher);
        setTitle(R.string.tabsPageHeader);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_tabs);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initArrays();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.reset_tabs_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.resetDebtsMenuItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure?");
                builder.setMessage("This change cannot be undone.");
                builder.setPositiveButton("I'm sure",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TabsHelper helper = new TabsHelper(TabsBrancherActivity.this);
                                helper.wipeData();
                                Toast.makeText(TabsBrancherActivity.this, "Debts have been wiped.", Toast.LENGTH_SHORT).show();
                                // Refresh page
                                Intent intent = new Intent(TabsBrancherActivity.this, TabsBrancherActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // smooth, still reloading the page but smoother.
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void initArrays(){
        TabsHelper helper = new TabsHelper(this);
        Cursor results = helper.getDistinctTabNames();
        tabNames.clear();
        while (results.moveToNext()){ // customer =
            tabNames.add(results.getString(0));
        }
        results.close();
        // To show the list from newest - oldest, not too efficient, better off finding a way to store it in reverse?
        initRecyclerView(); // puts it in
    }
    private void initRecyclerView(){
        Log.d(TAG, "TabsActivity: initiating recycler view)");
        RecyclerView recyclerView = findViewById(R.id.tabNameRecycler);
        TabsBrancherRecyclerViewAdapter adapater = new TabsBrancherRecyclerViewAdapter(this, tabNames);
        recyclerView.setAdapter(adapater);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
    }
    public void addNewTabButton(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name of new tab");
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: " + input.getText().toString());
                if (!input.getText().toString().isEmpty()) {
                    newTabString = input.getText().toString();
                    addNewTab();
                    initArrays();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        Log.d(TAG, "addNewTab: " + newTabString);
    }

    public void addNewTab(){
        TabsHelper helper = new TabsHelper(this);
        boolean successfulAdd = helper.addNewTab(newTabString); // addNewTab returns true when it successfully goes through
        if (!successfulAdd) {
            Toast.makeText(this, "The tab already exists!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "A new tab for " + newTabString + " has been created.", Toast.LENGTH_SHORT).show();
        }
    }
    public void wipeData(View view){
        TabsHelper helper = new TabsHelper(this);
        helper.wipeData();
        Toast.makeText(this, "Data wiped.", Toast.LENGTH_SHORT).show();
        initArrays();
    }
}
