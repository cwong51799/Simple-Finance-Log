package com.SimpleFinApp.myapplication.Tabs;

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

// This is a recycler class which formats a recycler view to display an individual person's debt entries in the Tabs section.

public class DebtDisplayRecyclerViewAdapter extends RecyclerView.Adapter<DebtDisplayRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "DebtDisplayRecycler";
    // Arrays of all the columns in the sqlite
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<Double> amounts = new ArrayList<>();
    private ArrayList<String> memos = new ArrayList<>();
    private Context mContext;
    private String tabName;

    public DebtDisplayRecyclerViewAdapter(Context context, String tabName, ArrayList<String> dates, ArrayList<Double> amounts, ArrayList<String> memos) {
        this.tabName = tabName;
        this.mContext = context;
        this.dates = dates;
        this.amounts = amounts;
        this.memos = memos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_debtitems, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder: called. New entry loaded as follows:");
        Log.d(TAG, "date: " + dates.get(position));
        Log.d(TAG, "amount:" + amounts.get(position));
        Log.d(TAG, "memo:" + memos.get(position));
        holder.date.setText(dates.get(position));
        holder.memo.setText(memos.get(position));
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        holder.amount.setText(formatter.format(amounts.get(position)));
        TextView btn = (TextView) holder.button;
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View parent = (View) v.getParent();
                TabsHelper helper = new TabsHelper(v.getContext());
                TextView date = parent.findViewById(R.id.tabDate);
                TextView amount = parent.findViewById(R.id.tabAmount);
                TextView memo = parent.findViewById(R.id.tabMemo);
                DebtEntry tab = new DebtEntry(new Date(date.getText().toString()), tabName, Double.parseDouble(amount.getText().toString().replace("$", "").replace(",", "")), memo.getText().toString());
                helper.removeEntry(tab); // this kinda sucks because it removes based on a NEARBY date, Amount, and Memo. Not by name. B/C I couldn't figure out how to get the name. Also tho, it doesnt mean much.
                // ************* 6/27/19 Get this to also refresh the page on its own afterward. Also make it so that the back page of each page is smoother (not everything leads to home).
                Intent intent = new Intent(mContext, DisplayDebtActivity.class);
                intent.putExtra("tabName", tabName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION); // smooth, still reloading the page but smoother.
                mContext.startActivity(intent);
                return true;
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
        TextView memo;
        TextView date;
        Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.tabAmount);
            memo = itemView.findViewById(R.id.tabMemo);
            date = itemView.findViewById(R.id.tabDate);
            button = itemView.findViewById(R.id.eraseDebtButton);
            parentLayout = itemView.findViewById(R.id.individualPurchase);
        }
    }
}
