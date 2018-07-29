package com.nayem.tourguide.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nayem.tourguide.R;
import com.nayem.tourguide.model.EventModel;
import com.nayem.tourguide.model.ExpenseModel;

import java.util.ArrayList;


public class EventExpenseAdapter extends ArrayAdapter<ExpenseModel> {
    private Context context;
    private ArrayList<ExpenseModel> expenseModels;
    private ExpenseModel expenseModel;


    public EventExpenseAdapter(Context context, ArrayList<ExpenseModel> expenseModels){
        super(context, R.layout.expense_row,expenseModels);
        this.context=context;
        this.expenseModels = expenseModels;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        expenseModel=expenseModels.get(position);
        convertView= LayoutInflater.from(getContext()).inflate(R.layout.expense_row,parent,false);

        TextView titleTextView= (TextView) convertView.findViewById(R.id.expenseRowTitleTextView);
        TextView amountTextView=(TextView)convertView.findViewById(R.id.expenseRowAmountTextView);


        titleTextView.setText(expenseModel.getTitle());
        amountTextView.setText(String.valueOf(expenseModel.getAmount()));
        return convertView;
    }
}
