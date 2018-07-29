package com.nayem.tourguide.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.nayem.tourguide.R;
import com.nayem.tourguide.adapter.NearbyAdapter;
import com.nayem.tourguide.api.NearbyApi;
import com.nayem.tourguide.model.modelNearby.NearbyPlace;
import com.nayem.tourguide.model.modelNearby.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearbyResultActivity extends AppCompatActivity {
    String type;
    String latitude;
    String longitude;
    String apiKey;

    NearbyApi nearbyApi;
    ProgressDialog progress;

    NearbyPlace nearby;
    NearbyAdapter nearbyAdapter;

    ListView nearbyListView;
    TextView headerTextView,totalTextView;

    ImageView backButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_result);
        nearbyListView = (ListView) findViewById(R.id.nearbyListView);
        headerTextView = (TextView) findViewById(R.id.headerTextView);
        //totalTextView = (TextView) findViewById(R.id.nearbyFooterTextView);
        backButton = (ImageView) findViewById(R.id.backToNearBy);

        latitude = String.valueOf(getIntent().getDoubleExtra("lati",0.0));
        longitude = String.valueOf(getIntent().getDoubleExtra("long",0.0));
        type = getIntent().getStringExtra("type");
        apiKey = "AIzaSyBwYeCE7p8XWuEFkF4_0Ieopab856luNjI";
        headerTextView.setText(type);
        type = type.toLowerCase();




        laibraryInitial();
        getData();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NearbyResultActivity.this,NearbyActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void laibraryInitial() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        nearbyApi = retrofit.create(NearbyApi.class);

    }
    public void getData(){
        progress = new ProgressDialog(NearbyResultActivity.this);
        progress.setCancelable(false);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait...");
        progress.show();

        String url = "json?location="+latitude+","+longitude+"&radius=1000&types="+type+"&key="+apiKey;
        //Log.e("----------------",url);
        Call<NearbyPlace>nearbyCall = nearbyApi.getNearbyPlace(url);
        //nearbyCall.enqueue(new Callback<Nearby>()
        nearbyCall.enqueue(new Callback<NearbyPlace>() {
            @Override
            public void onResponse(Call<NearbyPlace> call, Response<NearbyPlace> response) {
                nearby = response.body();
                //Toast.makeText(NearbyResultActivity.this, ""+nearby.getResults().size(), Toast.LENGTH_SHORT).show();
                nearbyAdapter = new NearbyAdapter(NearbyResultActivity.this,nearby.getResults());
                nearbyListView.setAdapter(nearbyAdapter);
                //totalTextView.setText(nearby.getResults().size()+" row found");
                nearbyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        double lat = nearby.getResults().get(i).getGeometry().getLocation().getLat();
                        double lng = nearby.getResults().get(i).getGeometry().getLocation().getLng();
                        startActivity(new Intent(NearbyResultActivity.this, MapDirectionActivity.class).
                        putExtra("lat",lat).putExtra("lng",lng));
                    }
                });


                progress.dismiss();
            }

            @Override
            public void onFailure(Call<NearbyPlace> call, Throwable t) {

                Toast .makeText(NearbyResultActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                progress.dismiss();
                NearbyResultActivity.this.startActivity(new Intent(NearbyResultActivity.this,NearbyActivity.class));
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.menubar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutMenu){
            sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("uid");
            //editor.remove("");
            editor.apply();
            editor.commit();
            Toast.makeText(getApplicationContext(),"Logout Successful",Toast.LENGTH_SHORT).show();
            Intent logoutIntent=new Intent(NearbyResultActivity.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        }
        if(item.getItemId() == R.id.aboutUsMenu) {
            Intent intent=new Intent(NearbyResultActivity.this,AboutUsActivity.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.homeMenu){
            Intent homeIntent=new Intent(NearbyResultActivity.this,ViewAllEventActivity.class);
            startActivity(homeIntent);
        }
        return true;

    }
}
