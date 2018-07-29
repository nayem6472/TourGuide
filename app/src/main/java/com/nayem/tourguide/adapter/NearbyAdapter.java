package com.nayem.tourguide.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nayem.tourguide.R;
import com.nayem.tourguide.model.ExpenseModel;
import com.nayem.tourguide.model.modelNearby.NearbyPlace;
import com.nayem.tourguide.model.modelNearby.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class NearbyAdapter extends ArrayAdapter<Result> {
    private Context context;
    private List<Result> places;
    private Result result;


    public NearbyAdapter(Context context, List<Result> results){
        super(context,R.layout.nearby_row,results);
        this.context=context;
        this.places = results;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        result=places.get(position);
        convertView= LayoutInflater.from(getContext()).inflate(R.layout.nearby_row,parent,false);

        ImageView typeIcon = (ImageView)convertView.findViewById(R.id.nearbyImageView);
        TextView nearbyTextView=(TextView)convertView.findViewById(R.id.nearbyPlageTextView);


        Picasso.with(context)
                .load(result.getIcon())
                .resize(50, 50)
                .centerCrop()
                .into(typeIcon);
        nearbyTextView.setText(result.getName());
        return convertView;
    }
}
