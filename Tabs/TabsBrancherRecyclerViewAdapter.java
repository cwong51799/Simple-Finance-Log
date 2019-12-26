package com.SimpleFinApp.myapplication.Tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SimpleFinApp.myapplication.R;

import java.text.NumberFormat;
import java.util.ArrayList;

public class TabsBrancherRecyclerViewAdapter extends RecyclerView.Adapter<TabsBrancherRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";
    // Arrays of all the columns in the sqlite
    private ArrayList<String> tabNames;
    Context mContext;

    public TabsBrancherRecyclerViewAdapter(Context context, ArrayList<String> tabNames) {
        mContext = context;
        this.tabNames = tabNames;
    }

    @NonNull
    @Override
    public TabsBrancherRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tabnames, parent, false);
        TabsBrancherRecyclerViewAdapter.ViewHolder holder = new TabsBrancherRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TabsBrancherRecyclerViewAdapter.ViewHolder holder, final int position){
        Log.d(TAG,"onBindViewHolder: called. Loading name: " + tabNames.get(position));
        TabsHelper helper = new TabsHelper(mContext);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        holder.tabNameBtn.setText(tabNames.get(position)); // sets their name
        holder.tabTotal.setText(formatter.format(helper.getTotalTabOf(tabNames.get(position)))); // sets the total tab desc
        holder.tabNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: called");
                Intent intent = new Intent(view.getContext(), DisplayDebtActivity.class);
                Bundle b = new Bundle();
                b.putString("tabName", tabNames.get(position)); // passes tabName as an argument
                intent.putExtras(b); //connects tabName to your next Intent
                view.getContext().startActivity(intent); // starts intent
            }
        });
    }
            // 6/27/19 - NEED TO FIGURE OUT HOW BUTTON CAN CALL A VARIABLE METHOD AND LOAD UP THE PAGE, WHAT CLASS DOES THIS GO IN?? HOW DOES THIS WORK??
    @Override
    public int getItemCount() {
        return tabNames.size(); // the amount of entries, they should all line up
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout parentLayout;
        TextView tabNameBtn;
        TextView tabTotal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tabNameBtn = itemView.findViewById(R.id.tabButton);
            tabTotal = itemView.findViewById(R.id.tabTotal);
            parentLayout = itemView.findViewById(R.id.individualPurchase);
        }
    }
}
