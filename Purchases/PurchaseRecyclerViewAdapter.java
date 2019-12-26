package com.SimpleFinApp.myapplication.Purchases;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SimpleFinApp.myapplication.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;

// This is an adapter class which uses the layout to display an unlimited amount of purchases.

public class PurchaseRecyclerViewAdapter extends RecyclerView.Adapter<PurchaseRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    // Arrays of all the columns in the sqlite
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<Double> amounts = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> memos = new ArrayList<>();
    private Context mContext;

    public PurchaseRecyclerViewAdapter(Context context, ArrayList<String> dates, ArrayList<Double> amounts, ArrayList<String> categories, ArrayList<String> memos) {
        this.mContext = context;
        this.dates = dates;
        this.amounts = amounts;
        this.categories = categories;
        this.memos = memos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_purchaseitems, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder: called. New entry loaded as follows:");
        Log.d(TAG, "date: " + dates.get(position));
        Log.d(TAG, "amount: " + amounts.get(position));
        Log.d(TAG, "category: " + categories.get(position));
        Log.d(TAG, "memo: " + memos.get(position));
        holder.date.setText(dates.get(position));
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        holder.amount.setText(formatter.format(amounts.get(position)));
        holder.category.setText(categories.get(position) + " -");
        holder.memo.setText(memos.get(position));
        TextView btn = (TextView) holder.button;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parent = (View) v.getParent();
                PurchaseHelper helper = new PurchaseHelper(v.getContext());
                TextView date = parent.findViewById(R.id.date);
                TextView amount = parent.findViewById(R.id.amount);
                TextView category = parent.findViewById(R.id.category);
                TextView memo = parent.findViewById(R.id.memo);
                Purchase purchase = new Purchase(new Date(date.getText().toString()), Double.parseDouble(amount.getText().toString().replace("$","").replace(",","")), category.getText().toString().replace(" -",""), memo.getText().toString());
                helper.removeEntry(purchase); // this kinda sucks because it removes based on a NEARBY date, Amount, and Memo. Not by name. B/C I couldn't figure out how to get the name. Also tho, it doesnt mean much.
                // ************* 6/27/19 Get this to also refresh the page on its own afterward. Also make it so that the back page of each page is smoother (not everything leads to home).
                Intent intent = new Intent(mContext, mContext.getClass());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // smooth, still reloading the page but smoother.
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return amounts.size(); // the amount of entries, they should all line up
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout parentLayout;
        TextView amount;
        TextView category;
        TextView memo;
        TextView date;
        View container;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            category = itemView.findViewById(R.id.category);
            memo = itemView.findViewById(R.id.memo);
            date = itemView.findViewById(R.id.date);
            container = itemView.findViewById(R.id.date);
            parentLayout = itemView.findViewById(R.id.individualPurchase);
            button = itemView.findViewById(R.id.removePurchaseBtn);
        }
    }
}
