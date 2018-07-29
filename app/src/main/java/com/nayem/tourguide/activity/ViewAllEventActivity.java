package com.nayem.tourguide.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nayem.tourguide.adapter.EventRowAdapter;
import com.nayem.tourguide.R;
import com.nayem.tourguide.database.DatabaseHelper;
import com.nayem.tourguide.database.EventManager;
import com.nayem.tourguide.gpsService.GPSTracker;
import com.nayem.tourguide.model.EventModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewAllEventActivity extends AppCompatActivity {
    private ListView eventListView;
    private EventRowAdapter eventRowAdapter;
    public EventManager eventManager;
    private ArrayList<EventModel> eventModels = null;
    private DatabaseHelper databaseHelper;

    Button nearbyButton, weatherButton, profileButton;

    GPSTracker gps;
    android.location.Location location;
    private double latitude;
    private double longitude;


    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_event);

        nearbyButton = (Button) findViewById(R.id.nearbyButton);
        weatherButton = (Button) findViewById(R.id.weatherButton);
        profileButton = (Button) findViewById(R.id.profileButton);
        eventListView = (ListView) findViewById(R.id.eventShowListView);

        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        int uid = sharedPreferences.getInt("uid", 0);

        //event list
        databaseHelper = new DatabaseHelper(this);
        eventModels = new ArrayList<>();
        eventManager = new EventManager(this);
        eventModels = eventManager.getUserAllEvent(uid);

        gps = new GPSTracker(this);

        if (eventModels != null) {
            eventRowAdapter = new EventRowAdapter(this, eventModels);
            eventListView.setAdapter(eventRowAdapter);
            eventListView.setItemsCanFocus(true);
        }

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewAllEventActivity.this, EventDetailsActivity.class);
                intent.putExtra("title", eventModels.get(position).getTitle());
                intent.putExtra("destination", eventModels.get(position).getDestination());
                intent.putExtra("budget", eventModels.get(position).getBudget());
                intent.putExtra("startDate", eventModels.get(position).getStartDate());
                intent.putExtra("endDate", eventModels.get(position).getEndDate());
                intent.putExtra("eid", eventModels.get(position).geteID());

                startActivity(intent);
            }
        });

        nearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                boolean network_enabled = false;

                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }

                if (!gps_enabled && !network_enabled) {
                    // notify user
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ViewAllEventActivity.this);
                    dialog.setMessage("Enable Location?");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            ViewAllEventActivity.this.startActivity(myIntent);
                            //get gps
                        }
                    });
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            // TODO Auto-generated method stub

                        }
                    });
                    dialog.show();
                } else {
                    Intent intent = new Intent(ViewAllEventActivity.this, NearbyActivity.class);
                    startActivity(intent);
                }


            }
        });

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

                Geocoder geocoder = new Geocoder(ViewAllEventActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    String cityName = addresses.get(0).getLocality();//.getAddressLine(0);
//                String stateName = addresses.get(0).getAddressLine(1);
//                String countryName = addresses.get(0).getAddressLine(2);
                    Intent intent = new Intent(ViewAllEventActivity.this, WeatherActivity.class);
                    intent.putExtra("cityName", cityName);
                    startActivity(intent);
                } else {
                    Toast.makeText(ViewAllEventActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAllEventActivity.this, UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }


    //using floating button
    public void AddEvent(View view) {
        Intent addEventIntent = new Intent(ViewAllEventActivity.this, AddEventActivity.class);
        startActivity(addEventIntent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.menu,menu);
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
            Intent logoutIntent=new Intent(ViewAllEventActivity.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        }
        if(item.getItemId() == R.id.aboutUsMenu) {
            Intent intent=new Intent(ViewAllEventActivity.this,AboutUsActivity.class);
            startActivity(intent);

        }
        return true;

    }
}
