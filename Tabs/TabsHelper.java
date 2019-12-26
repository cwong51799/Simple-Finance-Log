package com.SimpleFinApp.myapplication.Tabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.NumberFormat;
import java.util.Date;

public class TabsHelper extends SQLiteOpenHelper {
    // Columns in SQL
    public static final String Key_ID = "ID";
    public static final String Key_Date = "Date";
    public static final String Key_Amount = "Amount";
    public static final String Key_Name = "TabName";
    public static final String Key_Memo = "Memo";
    public static final String DBName = "Tabs";
    public static final String Table_Name = "Tabs";
    private static final String TAG = "TabsHelper";
    public static final int DBVersion = 1;
    String CREATE_PURCHASE_TABLE = "CREATE TABLE " + Table_Name + "(" + Key_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +  Key_Date + " TEXT," + Key_Amount + " DOUBLE," + Key_Name + " TEXT," + Key_Memo + " STRING" + ")"; // ID = 0, Date = 1, Amount = 2, TabName = 3, Memo = 4
    public TabsHelper(Context ctx){
        super(ctx, DBName, null, DBVersion);
    }
    public boolean addNewTab(String tabName){ // adds an entry to get the name into the database
        tabName = tabName.substring(0, 1).toUpperCase() + tabName.substring(1).toLowerCase();
        Log.d(TAG, "Inserting a new tab for " + tabName);
        // Get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create content values object and add values to it
        if (!checkForTabName(tabName)) { //  if it was not found, go through successfully
            ContentValues values = new ContentValues();
            values.put(Key_Date, new Date().toString());
            values.put(Key_Amount, 0.0);
            values.put(Key_Name, tabName);
            values.put(Key_Memo, "The tab has been created. Delete all entries to close the tab.");
            db.insert(Table_Name, null, values);
            return true;
        }
        else{
            return false; // otherwise it was found and nothing was added
        }
    }
    public void addToTab(DebtEntry tab){
        Log.d(TAG, "Inserting a debt record as follows:");
        // Get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create content values object and add values to it
        ContentValues values = new ContentValues();
        values.put(Key_Name, tab.getWhoOwesMe());
        Log.d(TAG, "name: " + tab.getWhoOwesMe());
        values.put(Key_Date, tab.getDateDebtAquired().toString());
        Log.d(TAG, "date: " + tab.getDateDebtAquired().toString());
        values.put(Key_Amount, tab.getAmount());
        NumberFormat formatter = NumberFormat.getNumberInstance();
        Log.d(TAG, "amount: " + tab.getAmount());
        values.put(Key_Memo, tab.getMemo());
        Log.d(TAG, "memo: " + tab.getMemo());
        db.insert(Table_Name, null, values);
        db.close();
    }

    public Cursor getDistinctTabNames(){
        // Get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a query
        String query = "SELECT DISTINCT TabName FROM " + Table_Name + " ORDER BY " + Key_Amount + " ASC";
        // Run the query
        Cursor result = db.rawQuery(query, null);
        // Return results
        return result;
    }
    public Cursor returnTabOf(String tabName){
        // Get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a query
        String query = "SELECT * FROM " + Table_Name + " WHERE " + Key_Name + " = '"+tabName+"'";
        // Run the query
        Cursor result = db.rawQuery(query, null);
        // Return results
        return result;
    }
    public double getTotalTabOf(String tabName){
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a query
        String query = "SELECT SUM(Amount) FROM " + Table_Name + " WHERE " + Key_Name + " = '"+tabName+"'";
        // Run the query
        Cursor result = db.rawQuery(query, null);
        double sum = 0;
        if (result.getCount() > 0){
            for (int i = 0; result.moveToNext(); i++) {
                if (result.getString(i) != null) {
                    sum += Double.parseDouble(result.getString(i));
                }
            }
        }
        db.close();
        return sum;
    }
    public void removeEntry(DebtEntry debt){ // delete an entry
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a query
        String query = "DELETE FROM " + Table_Name + " WHERE " + Key_Name + " = '" + debt.getWhoOwesMe() + "' AND "+ Key_Amount + " = " + debt.getAmount() + " AND " + Key_Memo + " = '" + debt.getMemo() + "' AND " + Key_Date + " = '" + debt.getDateDebtAquired().toString()+"'";
        Log.d(TAG, "removeEntry: Executing query: " + query);
        // Run the query
        db.execSQL(query);
        db.close();
    }

    public boolean checkForTabName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a query
        String query = "SELECT 1 FROM " + Table_Name + " WHERE " + Key_Name + " = '"+name+"'";
        // Run the query
        Cursor result = db.rawQuery(query, null);
        // Return results
        return (((result != null) && (result.getCount() > 0))); // returns true if it is was found
    }
    void wipeData(){
        String query = "DELETE FROM " + Table_Name;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PURCHASE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
