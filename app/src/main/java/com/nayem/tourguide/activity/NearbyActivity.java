package com.nayem.tourguide.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nayem.tourguide.R;
import com.nayem.tourguide.gpsService.GPSTracker;
import com.nayem.tourguide.model.Location;

public class NearbyActivity extends AppCompatActivity {

    Button restaurantButton,cafeButton,hospitalButton,bankButton;

    public static String RESTAURANT = "Food";
    public static String CAFE = "Cafe";
    public static String HOSPITAL = "Hospital";
    public static String BANK = "Bank";

    GPSTracker gps;
    android.location.Location location;
    private double latitude;
    private double longitude;

    ProgressDialog progress;

    ImageView backToHome;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        gps = new GPSTracker(NearbyActivity.this);

        restaurantButton= (Button) findViewById(R.id.restaurantButton);
        cafeButton= (Button) findViewById(R.id.cafeButton);
        hospitalButton= (Button) findViewById(R.id.hospitalButton);
        bankButton= (Button) findViewById(R.id.bankButton);

        backToHome= (ImageView) findViewById(R.id.backToHomeFromNearBy);
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent=new Intent(NearbyActivity.this,ViewAllEventActivity.class);
                startActivity(homeIntent);
            }
        });

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
           // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        restaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NearbyActivity.this,NearbyResultActivity.class);
                intent.putExtra("lati",latitude);
                intent.putExtra("long",longitude);
                intent.putExtra("type",RESTAURANT);
                startActivity(intent);
                finish();
            }
        });

        cafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NearbyActivity.this,NearbyResultActivity.class);
                intent.putExtra("lati",latitude);
                intent.putExtra("long",longitude);
                intent.putExtra("type",CAFE);
                startActivity(intent);
                finish();
            }
        });
        hospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NearbyActivity.this,NearbyResultActivity.class);
                intent.putExtra("lati",latitude);
                intent.putExtra("long",longitude);
                intent.putExtra("type",HOSPITAL);
                startActivity(intent);
                finish();
            }
        });
        bankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NearbyActivity.this,NearbyResultActivity.class);
                intent.putExtra("lati",latitude);
                intent.putExtra("long",longitude);
                intent.putExtra("type",BANK);
                startActivity(intent);
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
            Intent logoutIntent=new Intent(NearbyActivity.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        }
        if(item.getItemId() == R.id.aboutUsMenu) {
            Intent intent=new Intent(NearbyActivity.this,AboutUsActivity.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.homeMenu){
            Intent homeIntent=new Intent(NearbyActivity.this,ViewAllEventActivity.class);
            startActivity(homeIntent);
        }
        return true;

    }
}
