package com.SimpleFinApp.myapplication.Purchases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.Random;


// This class is used to create and interact with the SQLite database "Purchases"

public class PurchaseHelper extends SQLiteOpenHelper {
    // Columns in SQL
    public static final String Key_ID = "ID";
    public static final String Key_Date = "Date";
    public static final String Key_Amount = "Amount";
    public static final String Key_Category = "Category";
    public static final String Key_Memo = "Memo";
    public static final String Key_DateAsLong = "DateAsLong";
    public static final String DBName = "Purchases";
    public static final String Table_Name = "Purchases";
    private static final String TAG = "PurchaseHelper";
    public static final int DBVersion = 1;
    String CREATE_PURCHASE_TABLE = "CREATE TABLE " + Table_Name + "(" + Key_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +  Key_Date + " TEXT," + Key_DateAsLong + " LONG," + Key_Amount + " DOUBLE," + Key_Category + " TEXT," + Key_Memo + " STRING" + ")"; // ID = 0, Date = 1, DateAsNum = 2, Amount = 3, Category = 4, Memo = 5
    public PurchaseHelper (Context ctx){
        super(ctx, DBName, null, DBVersion);
    }
    public void addPurchase(Purchase purchaseObj){
        Log.d(TAG, "Inserting a purchase record.");
        // Get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create content values object and add values to it
        ContentValues values = new ContentValues();
        values.put(Key_Date, purchaseObj.getDateOfPurchase().toString());
        values.put(Key_Memo, purchaseObj.getMemo());
        values.put(Key_DateAsLong, purchaseObj.getDateAsLong());
        values.put(Key_Amount, purchaseObj.getAmount());
        values.put(Key_Category, purchaseObj.getCategory());
        values.put(Key_Memo, purchaseObj.getMemo());
        db.insert(Table_Name, null, values);
        db.close();
    }
    public Cursor getAllPurchases(){ // cursors are locked into a certain row, return all purchases from most recent -> oldest
        Log.d(TAG, "Reading all dates.");
        // Get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a query
        String query = "SELECT * FROM " + Table_Name + " ORDER BY " + Key_DateAsLong + " ASC";
        // Run the query
        Cursor result = db.rawQuery(query, null);
        // Return results
        return result;
    }
    public Cursor getWeekOfPurchasesFrom(Date date){ // cursors are locked into a certain row
        Log.d(TAG, "Reading a week of purchases from " + date.toString()); // why is it 3919????
        // Get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a query
        Long dateAsNum = (date.getTime() - 59958147600000L); // offset because for some reason its 1900 years ahead!?!??
        Long weekFromDate = dateAsNum + 627600000; // 1 week worth of miliseconds
        Log.d(TAG, "Searching for purchases between  " + new Date(dateAsNum).toString() + " and " + new Date(weekFromDate).toString());
        String query = "SELECT * FROM " + Table_Name + " WHERE " + Key_DateAsLong +" BETWEEN " + dateAsNum + " AND " + weekFromDate + " ORDER BY " + Key_DateAsLong + " ASC";
        // Run the query
        Cursor result = db.rawQuery(query, null);
        Log.d(TAG, "getWeekOfPurchasesFrom: Found " + result.getCount() + " results.");
        db.close();
        // Return results
        return result;
    }
    public Cursor getDayOfPurchasesFrom(Date date){ // cursors are locked into a certain row
        Log.d(TAG, "Reading a day of purchases from " + date.toString()); // why is it 3919????
        // Get reference to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a query
        Long dateAsNum = (date.getTime() - 59958147600000L); // offset because for some reason its 1900 years ahead!?!??
        Long dayFromDate = dateAsNum + 86400000; // 1 day worth of miliseconds
        Log.d(TAG, "Searching for purchases between  " + new Date(dateAsNum).toString() + " and " + new Date(dayFromDate).toString());
        String query = "SELECT * FROM " + Table_Name + " WHERE " + Key_DateAsLong +" BETWEEN " +  dateAsNum + " AND " + dayFromDate + " ORDER BY " + Key_DateAsLong + " ASC";
        // Run the query
        Cursor result = db.rawQuery(query, null);
        Log.d(TAG, "getWeekOfPurchasesFrom: Found " + result.getCount() + " results.");
        db.close();
        // Return results
        return result;
    }
    double getTotalAmountSpent() {
        Log.d(TAG, "Retrieving total amount spent.");
        String query = "SELECT SUM(Amount) FROM Purchases";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery(query, null);
        double sum = 0;
        if (result.getCount() > 0) {
            for (int i = 0; result.moveToNext(); i++) {
                if (result.getString(i) != null) {
                    sum += Double.parseDouble(result.getString(i));
                }
            }
        }
        db.close();
        return sum;
    }

    double getTotalAmountSpentOn(String category){
        Log.d(TAG, "Retrieving total amount spent on " + category);
        String query = "SELECT SUM(Amount) FROM Purchases WHERE " + Key_Category + " = '" + category+"'";
        SQLiteDatabase db = this.getWritableDatabase();
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
    public void removeEntry(Purchase purchase){ // delete an entry
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a query
        String query = "DELETE FROM " + Table_Name + " WHERE " + Key_Amount + " = " + purchase.getAmount() + " AND " + Key_Memo + " = '" + purchase.getMemo() + "' AND " +Key_Category+" = '" + purchase.getCategory() + "' AND " + Key_Date + " = '" + purchase.getDateOfPurchase()+"'";
        Log.d(TAG, "removeEntry: Executing query: " + query);
        // Run the query
        db.execSQL(query);
        db.close();
    }

    String getFirstPurchase(){
        Log.d(TAG, "Retrieving the first purchase date.");
        String query = "SELECT " + Key_DateAsLong +" FROM Purchases"; // the date of the first entry
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery(query, null);
        String firstPurchaseDate;
        result.moveToFirst();
        try {
            firstPurchaseDate = new Date(result.getLong(0)).toString();
            String monthday = firstPurchaseDate.substring(4, 10);
            String year = firstPurchaseDate.substring(23);
            db.close();
            Log.d(TAG, "getFirstPurchase: " + firstPurchaseDate);
            return monthday + " " + year;
        }
        catch(Exception ex){
            Log.d(TAG, "getFirstPurchase: " + ex);
            firstPurchaseDate = "error";
            db.close();
            return firstPurchaseDate;
        }
    }
    void wipeData(){
        String query = "DELETE FROM " + Table_Name;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void populatePurchases() {
        // public Purchase(Date dateOfPurchase, double amount, String category, String memo){
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
            addPurchase(new Purchase(new Date(randDateAsLong),randAmt,randCategory,"testing entry " + i));
        }
    }
    public String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    SQLiteDatabase getDatabase(){
        return this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PURCHASE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
