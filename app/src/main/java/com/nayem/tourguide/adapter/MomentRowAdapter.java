package com.nayem.tourguide.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nayem.tourguide.R;
import com.nayem.tourguide.model.MomentModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class MomentRowAdapter extends ArrayAdapter<MomentModel> {
    private Context context;
    private ArrayList<MomentModel> momentModels;
    private MomentModel momentModel;
    Bitmap imageBitmap;

    public MomentRowAdapter(Context context, ArrayList<MomentModel> momentModels) {
        super(context,R.layout.moment_row,momentModels);
        this.context=context;
        this.momentModels=momentModels;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        momentModel=momentModels.get(position);
        convertView= LayoutInflater.from(getContext()).inflate(R.layout.moment_row,parent,false);
        ImageView imageView= (ImageView) convertView.findViewById(R.id.currentCustomImageView);
        TextView titleTV= (TextView) convertView.findViewById(R.id.momentTitleTextView);
        TextView dateMomentTV= (TextView) convertView.findViewById(R.id.momentDateTextView);

        String imagePath=momentModel.getImage();


        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(imagePath,options);

        int photoW=options.outWidth;
        int photoH=options.outHeight;
        int scaleFactor=Math.min( 160, 160);

        options.inJustDecodeBounds=false;
        options.inSampleSize=scaleFactor;

        imageBitmap=BitmapFactory.decodeFile(imagePath,options);
        imageView.getLayoutParams().height = 200;
        imageView.getLayoutParams().width = 260;
        imageView.requestLayout();
        imageView.setImageBitmap(imageBitmap);

        titleTV.setText(momentModel.getTitle().toString());
        dateMomentTV.setText(momentModel.getDate().toString());

        return convertView;

    }
}
