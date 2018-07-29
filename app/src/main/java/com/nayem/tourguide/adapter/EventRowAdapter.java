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

import java.util.ArrayList;

import static com.nayem.tourguide.R.id.toDateTextView;


public class EventRowAdapter extends ArrayAdapter<EventModel> {
    private Context context;
    private ArrayList<EventModel> eventModels;
    private EventModel eventModel;


    public EventRowAdapter(Context context, ArrayList<EventModel> eventModels){
        super(context, R.layout.event_row,eventModels);
        this.context=context;
        this.eventModels=eventModels;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        eventModel=eventModels.get(position);
        convertView= LayoutInflater.from(getContext()).inflate(R.layout.event_row,parent,false);

        TextView destinationTV= (TextView) convertView.findViewById(R.id.destinationShowTextView);
        TextView fromDateTV=(TextView)convertView.findViewById(R.id.fromDateTextView);
        TextView toDateTV= (TextView) convertView.findViewById(R.id.toDateTextView);
        TextView amountTV= (TextView) convertView.findViewById(R.id.estimatedAmountTextView);

        destinationTV.setText(eventModel.getDestination().toString());
        fromDateTV.setText(eventModel.getStartDate());
        toDateTV.setText(eventModel.getEndDate());
        amountTV.setText(eventModel.getBudget()+" TK");
        return convertView;
    }
}
