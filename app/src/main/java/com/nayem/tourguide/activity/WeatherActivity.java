package com.nayem.tourguide.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nayem.tourguide.R;
import com.nayem.tourguide.api.WeatherApi;
import com.nayem.tourguide.model.Weather;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    WeatherApi weatherApi;

    ScrollView scrollView;

    EditText focusEditText;
    Button searchButton;

    TextView avgTextView,maxTextView,minTextView,textTextView,windTextView, poweredByTextView;

    ImageView minImageView,maxImageView,textImageView;

    LinearLayout mainLayout;
    RelativeLayout relativeLayout;
    RelativeLayout internetLayout;

    EditText cityTextView;

    Call<Weather>weatherCall;
    Weather weather;

    Window window;

    private String cityName;

    AutoCompleteTextView city;
    ImageView searchCity;
    Button refreshButton;

    //-----------------Forecast References------------------------//
    TextView dayOneTV;
    TextView dayTwoTV;
    TextView dayThreeTV;
    TextView dayFourTV;
    TextView dayFiveTV;
    TextView daySixTV;
    TextView daySevenTV;

    TextView dayOneHighTV;
    TextView dayTwoHighTV;
    TextView dayThreeHighTV;
    TextView dayFourHighTV;
    TextView dayFiveHighTV;
    TextView daySixHighTV;
    TextView daySevenHighTV;

    TextView dayOneLowTV;
    TextView dayTwoLowTV;
    TextView dayThreeLowTV;
    TextView dayFourLowTV;
    TextView dayFiveLowTV;
    TextView daySixLowTV;
    TextView daySevenLowTV;

    ImageView dayOneIV;
    ImageView dayTwoIV;
    ImageView dayThreeIV;
    ImageView dayFourIV;
    ImageView dayFiveIV;
    ImageView daySixIV;
    ImageView daySevenIV;
    //----------------------End of Forecast--------------------------//

    //------------------------Weather Details--------------------//

    TextView humidityTV, visibilityTV, pressureTV,sunSetTV, sunRiseTV;
    ImageView detailsImageView;
    //------------------------End of Weatehr Details---------------------//

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initializeReferences();
        //cityName = getIntent().getStringExtra("cityName");
        //cityTextView.setText(cityName);
        hideKeyboard();

        workForRefresh();
        //getData();

        getAllCityName();

        city.setThreshold(1);
        final ArrayAdapter<String> cityAdapter=new ArrayAdapter<String>(WeatherActivity.this,android.R.layout.simple_dropdown_item_1line,data);
        city.setAdapter(cityAdapter);


        searchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String cityName = city.getText().toString();
                    String[] splitCity = cityName.split(",");
                    //Toast.makeText(StartActivity.this, splitCity[0] + " " + splitCity[1], Toast.LENGTH_LONG).show();
                    getData(splitCity[0]);
                    //cityNam = splitCity[0];
                    hideKeyboard();

                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Select City Name Properly", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }finally {
                    city.setAdapter(cityAdapter);
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String cityName = city.getText().toString();
                    String[] splitCity = cityName.split(",");
                    //Toast.makeText(StartActivity.this, splitCity[0] + " " + splitCity[1], Toast.LENGTH_LONG).show();
                    getData(splitCity[0]);
                    //cityNam = splitCity[0];
                    hideKeyboard();

                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Select City Name Properly", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }finally {
                    city.setAdapter(cityAdapter);
                }
            }
        });

        /*cityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityTextView.setText(null);
            }
        });*/

    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(ViewAllEventActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        focusEditText.setVisibility(View.VISIBLE);
        focusEditText.setFocusableInTouchMode(true);
        focusEditText.requestFocus();
        focusEditText.setVisibility(View.INVISIBLE);
    }

    private void workForRefresh() {
        internetLayout.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (Build.VERSION.SDK_INT >= 21) {
            window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark,getTheme()));
        }
        laibraryInitial();



    }

    InputStream inputStream;
    String[] ids;

    ArrayList<String> data;

    private void getAllCityName() {
        data=new ArrayList<>();


        inputStream=getResources().openRawResource(R.raw.city);
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));


        try {
            String csvLine;
            while ((csvLine =reader.readLine())!=null){
                ids=csvLine.split(",");

                try {
                    if (ids[0].length()>0 && ids[1].length() > 0){
                        data.add(ids[0].toString()+", "+ids[1]);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void getData(String cityName) {

        progress = new ProgressDialog(WeatherActivity.this);
        progress.setCancelable(false);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait...");
        progress.show();

        //cityName = cityTextView.getText().toString();
        String str1 = "v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D\"";
        String str2 = "%2C%20ak\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        weatherCall = weatherApi.getWeatherData(str1+cityName+str2);


        weatherCall.enqueue(new Callback<Weather>() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                weather = response.body();

                focusEditText.setVisibility(View.VISIBLE);
                focusEditText.setFocusableInTouchMode(true);
                focusEditText.requestFocus();
                focusEditText.setVisibility(View.INVISIBLE);

                //-----------------------Basic Information-------------------------//
                setImages();

                setInitialInfo();

                //-----------------------End of Basic Information-----------------//


//                ------------------------Forecast-----------------------------//
                for(int i=0;i<7;i++){
                    switch (i){
                        case 0:DayOne();
                            break;
                        case 1:DayTwo();
                            break;
                        case 2:DayThree();
                            break;
                        case 3:DayFour();
                            break;
                        case 4:DayFive();
                            break;
                        case 5:DaySix();
                            break;
                        case 6:DaySeven();
                            break;
                    }


                    //---------------------End of Forecast---------------//

                    //-----------------------Weather Details-------------------//
                    getWeatherDetails();
                    //----------------------End of Weather Details------------------//
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                internetLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                //workForRefresh();
//                Toast.makeText(StartActivity.this, "Sorry!!!\nPlease check your internet connection.", Toast.LENGTH_SHORT).show();

            }
        });

        progress.dismiss();
    }

    public void laibraryInitial() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://query.yahooapis.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        weatherApi = retrofit.create(WeatherApi.class);
    }

    public void initializeReferences(){
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        focusEditText = (EditText)findViewById(R.id.focusEditText);

        //searchButton = (Button) findViewById(R.id.searchButton);

        avgTextView = (TextView) findViewById(R.id.avgTextView);
        maxTextView = (TextView) findViewById(R.id.maxTextView);
        minTextView = (TextView) findViewById(R.id.minTextView);
        textTextView = (TextView) findViewById(R.id.textTextView);
        windTextView = (TextView) findViewById(R.id.windTextView);
        poweredByTextView = (TextView) findViewById(R.id.poweredByTextView);

        minImageView = (ImageView) findViewById(R.id.minImageView);
        maxImageView = (ImageView) findViewById(R.id.maxImageView);
        textImageView = (ImageView) findViewById(R.id.textImageView);


        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        internetLayout = (RelativeLayout)findViewById(R.id.internetLayout);

        //cityTextView = (EditText) findViewById(R.id.cityEditText);


        searchCity= (ImageView) findViewById(R.id.searchCity);
        city= (AutoCompleteTextView) findViewById(R.id.cityName);
        //avgTemp= (TextView) findViewById(R.id.averageCityName);
        refreshButton= (Button) findViewById(R.id.refreshButton);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = height-80;
//        params.width = 100;
        relativeLayout.setLayoutParams(params);

//        relativeLayout.setMinimumHeight(height);


//        ---------------------Forecast----------------------------//
        dayOneTV= (TextView) findViewById(R.id.dayOneTextView);
        dayTwoTV= (TextView) findViewById(R.id.dayTwoTextView);
        dayThreeTV= (TextView) findViewById(R.id.dayThreeTextView);
        dayFourTV= (TextView) findViewById(R.id.dayFourTextView);
        dayFiveTV= (TextView) findViewById(R.id.dayFiveTextView);
        daySixTV= (TextView) findViewById(R.id.daySixTextView);
        daySevenTV= (TextView) findViewById(R.id.daySevenTextView);

        dayOneHighTV= (TextView) findViewById(R.id.dayOneHighTempTextView);
        dayTwoHighTV= (TextView) findViewById(R.id.dayTwoHighTempTextView);
        dayThreeHighTV= (TextView) findViewById(R.id.dayThreeHighTempTextView);
        dayFourHighTV= (TextView) findViewById(R.id.dayFourHighTempTextView);
        dayFiveHighTV= (TextView) findViewById(R.id.dayFiveHighTempTextView);
        daySixHighTV= (TextView) findViewById(R.id.daySixHighTempTextView);
        daySevenHighTV= (TextView) findViewById(R.id.daySevenHighTempTextView);

        dayOneLowTV= (TextView) findViewById(R.id.dayOneLowTempTextView);
        dayTwoLowTV= (TextView) findViewById(R.id.dayTwoLowTempTextView);
        dayThreeLowTV= (TextView) findViewById(R.id.dayThreeLowTempTextView);
        dayFourLowTV= (TextView) findViewById(R.id.dayFourLowTempTextView);
        dayFiveLowTV= (TextView) findViewById(R.id.dayFiveLowTempTextView);
        daySixLowTV= (TextView) findViewById(R.id.daySixLowTempTextView);
        daySevenLowTV= (TextView) findViewById(R.id.daySevenLowTempTextView);


        dayOneIV= (ImageView) findViewById(R.id.dayOneImageView);
        dayTwoIV= (ImageView) findViewById(R.id.dayTwoImageView);
        dayThreeIV= (ImageView) findViewById(R.id.dayThreeImageView);
        dayFourIV= (ImageView) findViewById(R.id.dayFourImageView);
        dayFiveIV= (ImageView) findViewById(R.id.dayFiveImageView);
        daySixIV= (ImageView) findViewById(R.id.daySixImageView);
        daySevenIV= (ImageView) findViewById(R.id.daySevenImageView);
        // ---------------------------------------------------------------//

        //-------------------Weather Details Variable Initialize------------//
        humidityTV = (TextView) findViewById(R.id.humidiValueTV);
        visibilityTV = (TextView) findViewById(R.id.visibilyValueTV);
        pressureTV = (TextView) findViewById(R.id.pressureValueTV);
        sunRiseTV = (TextView) findViewById(R.id.sunriseValuTV);
        sunSetTV = (TextView) findViewById(R.id.sunsetValuTV);

        detailsImageView = (ImageView) findViewById(R.id.detailsImgIV);
        //------------------------------------------------------------------------//

    }

    //---------------------------Basic Information----------------------------//
    public void setImages(){
        if(weather.getQuery().getResults().getChannel().getItem()
                .getCondition().getText().equals("Sunny")){
            mainLayout.setBackgroundResource(R.drawable.sunny_image);
            textImageView.setBackgroundResource(R.drawable.sunny_logo);
            detailsImageView.setBackgroundResource(R.drawable.sunny_logo);
//                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark,getTheme()));
        }else if(weather.getQuery().getResults().getChannel().getItem()
                .getCondition().getText().equals("Mostly Sunny")){
            mainLayout.setBackgroundResource(R.drawable.sunny_image);
            textImageView.setBackgroundResource(R.drawable.sunny_logo);
            detailsImageView.setBackgroundResource(R.drawable.sunny_logo);
//                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark,getTheme()));
        }else if(weather.getQuery().getResults().getChannel().getItem()
                .getCondition().getText().equals("Partly Sunny")){
            mainLayout.setBackgroundResource(R.drawable.sunny_image);
            textImageView.setBackgroundResource(R.drawable.sunny_logo);
            detailsImageView.setBackgroundResource(R.drawable.sunny_logo);
//                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark,getTheme()));
        }else if(weather.getQuery().getResults().getChannel().getItem()
                .getCondition().getText().equals("Cloudy")){
            mainLayout.setBackgroundResource(R.drawable.cloudy_image);
            textImageView.setBackgroundResource(R.drawable.cloud_logo);
            detailsImageView.setBackgroundResource(R.drawable.cloud_logo);
//                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkBlack,getTheme()));
        }else if(weather.getQuery().getResults().getChannel().getItem()
                .getCondition().getText().equals("Clear")){
            mainLayout.setBackgroundResource(R.drawable.night_image);
            textImageView.setBackgroundResource(R.drawable.clear_moon);
            detailsImageView.setBackgroundResource(R.drawable.clear_moon);
//                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkBlack,getTheme()));
        }else if(weather.getQuery().getResults().getChannel().getItem()
                .getCondition().getText().equals("Mostly Clear")){
            mainLayout.setBackgroundResource(R.drawable.night_image);
            textImageView.setBackgroundResource(R.drawable.clear_moon);
            detailsImageView.setBackgroundResource(R.drawable.clear_moon);
//                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkBlack,getTheme()));
        }else if(weather.getQuery().getResults().getChannel().getItem()
                .getCondition().getText().equals("Partly Clear")){
            mainLayout.setBackgroundResource(R.drawable.night_image);
            textImageView.setBackgroundResource(R.drawable.clear_moon);
            detailsImageView.setBackgroundResource(R.drawable.clear_moon);
//                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkBlack,getTheme()));
        }else if(weather.getQuery().getResults().getChannel().getItem()
                .getCondition().getText().equals("Partly Cloudy")){
            mainLayout.setBackgroundResource(R.drawable.cloudy_image);
            textImageView.setBackgroundResource(R.drawable.clear_logo);
            detailsImageView.setBackgroundResource(R.drawable.clear_logo);
//                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkBlack,getTheme()));
        }else if(weather.getQuery().getResults().getChannel().getItem()
                .getCondition().getText().equals("Mostly Cloudy")){
            mainLayout.setBackgroundResource(R.drawable.cloudy_image);
            textImageView.setBackgroundResource(R.drawable.cloud_logo);
            detailsImageView.setBackgroundResource(R.drawable.cloud_logo);
//                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkBlack,getTheme()));
        }
    }

    public void setInitialInfo(){
        String avgValue=weather.getQuery().getResults().getChannel().getItem().getCondition().getTemp();
        int avg=Integer.parseInt(avgValue);
        int resultAvg = convertFahToCel(avg);
        avgTextView.setText(String.valueOf(resultAvg)+(char) 0x00B0+"C");
        //avgTemp.setText(cityNam);

        String highValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh();
        int highF=Integer.parseInt(highValue);
        int resultHigh = convertFahToCel(highF);
        maxTextView.setText(String.valueOf(resultHigh)+(char) 0x00B0);
        maxImageView.setBackgroundResource(R.drawable.arrow_up);

        String lowValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow();
        int lowF=Integer.parseInt(lowValue);
        int resultLow = convertFahToCel(lowF);
        minTextView.setText(String.valueOf(resultLow)+(char) 0x00B0);
        minImageView.setBackgroundResource(R.drawable.arrow_down);

        textTextView.setText(weather.getQuery().getResults().getChannel()
                .getItem().getCondition().getText());


        windTextView.setText(weather.getQuery().getResults().getChannel().getWind().getSpeed()+
                " "+weather.getQuery().getResults().getChannel().getUnits().getSpeed());
    }
    //------------------------------End of Basic Information------------------//


    //  ---------------------------------ForeCast GetDays-------------------------//
    public void DayOne(){
        String dayOne = weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getDay();
        String dayOneName = getDay(dayOne);
        dayOneTV.setText(dayOneName);
        //dayOneHighTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh()+ (char) 0x00B0);
        // dayOneLowTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow()+ (char) 0x00B0);

        String highValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh();
        int highF=Integer.parseInt(highValue);
        int resultHigh = convertFahToCel(highF);
        dayOneHighTV.setText(String.valueOf(resultHigh)+(char) 0x00B0);

        String lowValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow();
        int lowF=Integer.parseInt(lowValue);
        int resultLow = convertFahToCel(lowF);
        dayOneLowTV.setText(String.valueOf(resultLow)+(char) 0x00B0);
        if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getText().equals("Sunny")) {
            dayOneIV.setImageResource(R.drawable.sunny_logo);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getText().equals("Mostly Sunny")) {
            dayOneIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getText().equals("Partly Sunny")) {
            dayOneIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getText().equals("Clear")) {
            dayOneIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getText().equals("Mostly Clear")) {
            dayOneIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getText().equals("Partly Clear")) {
            dayOneIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getText().equals("Cloudy")) {
            dayOneIV.setImageResource(R.drawable.cloud_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getText().equals("Partly Cloudy")) {
            dayOneIV.setImageResource(R.drawable.clear_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getText().equals("Mostly Cloudy")) {
            dayOneIV.setImageResource(R.drawable.cloud_logo);
        }

    }
    public void DayTwo(){
        String dayOne = weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getDay();
        String dayOneName = getDay(dayOne);
        dayTwoTV.setText(dayOneName);
        //dayOneHighTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh()+ (char) 0x00B0);
        // dayOneLowTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow()+ (char) 0x00B0);

        String highValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getHigh();
        int highF=Integer.parseInt(highValue);
        int resultHigh = convertFahToCel(highF);
        dayTwoHighTV.setText(String.valueOf(resultHigh)+(char) 0x00B0);

        String lowValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getLow();
        int lowF=Integer.parseInt(lowValue);
        int resultLow = convertFahToCel(lowF);
        dayTwoLowTV.setText(String.valueOf(resultLow)+(char) 0x00B0);

        if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getText().equals("Sunny")) {
            dayTwoIV.setImageResource(R.drawable.sunny_logo);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getText().equals("Mostly Sunny")) {
            dayTwoIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getText().equals("Partly Sunny")) {
            dayTwoIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getText().equals("Clear")) {
            dayTwoIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getText().equals("Mostly Clear")) {
            dayTwoIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getText().equals("Partly Clear")) {
            dayTwoIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getText().equals("Cloudy")) {
            dayTwoIV.setImageResource(R.drawable.cloud_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getText().equals("Partly Cloudy")) {
            dayTwoIV.setImageResource(R.drawable.clear_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(1).getText().equals("Mostly Cloudy")) {
            dayTwoIV.setImageResource(R.drawable.cloud_logo);
        }
    }
    public void DayThree(){
        String dayOne = weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getDay();
        String dayOneName = getDay(dayOne);
        dayThreeTV.setText(dayOneName);
        //dayOneHighTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh()+ (char) 0x00B0);
        // dayOneLowTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow()+ (char) 0x00B0);

        String highValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getHigh();
        int highF=Integer.parseInt(highValue);
        int resultHigh = convertFahToCel(highF);
        dayThreeHighTV.setText(String.valueOf(resultHigh)+(char) 0x00B0);

        String lowValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getLow();
        int lowF=Integer.parseInt(lowValue);
        int resultLow = convertFahToCel(lowF);
        dayThreeLowTV.setText(String.valueOf(resultLow)+(char) 0x00B0);

        if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getText().equals("Sunny")) {
            dayThreeIV.setImageResource(R.drawable.sunny_logo);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getText().equals("Mostly Sunny")) {
            dayThreeIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getText().equals("Partly Sunny")) {
            dayThreeIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getText().equals("Clear")) {
            dayThreeIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getText().equals("Mostly Clear")) {
            dayThreeIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getText().equals("Partly Clear")) {
            dayThreeIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getText().equals("Cloudy")) {
            dayThreeIV.setImageResource(R.drawable.cloud_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getText().equals("Partly Cloudy")) {
            dayThreeIV.setImageResource(R.drawable.clear_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(2).getText().equals("Mostly Cloudy")) {
            dayThreeIV.setImageResource(R.drawable.cloud_logo);
        }

    }

    public void DayFour(){
        String dayOne = weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getDay();
        String dayOneName = getDay(dayOne);
        dayFourTV.setText(dayOneName);
        //dayOneHighTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh()+ (char) 0x00B0);
        // dayOneLowTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow()+ (char) 0x00B0);

        String highValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getHigh();
        int highF=Integer.parseInt(highValue);
        int resultHigh = convertFahToCel(highF);
        dayFourHighTV.setText(String.valueOf(resultHigh)+(char) 0x00B0);

        String lowValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getLow();
        int lowF=Integer.parseInt(lowValue);
        int resultLow = convertFahToCel(lowF);
        dayFourLowTV.setText(String.valueOf(resultLow)+(char) 0x00B0);

        if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getText().equals("Sunny")) {
            dayFourIV.setImageResource(R.drawable.sunny_logo);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getText().equals("Mostly Sunny")) {
            dayFourIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getText().equals("Partly Sunny")) {
            dayFourIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getText().equals("Clear")) {
            dayFourIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getText().equals("Mostly Clear")) {
            dayFourIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getText().equals("Partly Clear")) {
            dayFourIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getText().equals("Cloudy")) {
            dayFourIV.setImageResource(R.drawable.cloud_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getText().equals("Partly Cloudy")) {
            dayFourIV.setImageResource(R.drawable.clear_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(3).getText().equals("Mostly Cloudy")) {
            dayFourIV.setImageResource(R.drawable.cloud_logo);
        }

    }

    public void DayFive(){
        String dayOne = weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getDay();
        String dayOneName = getDay(dayOne);
        dayFiveTV.setText(dayOneName);
        //dayOneHighTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh()+ (char) 0x00B0);
        // dayOneLowTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow()+ (char) 0x00B0);

        String highValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getHigh();
        int highF=Integer.parseInt(highValue);
        int resultHigh = convertFahToCel(highF);
        dayFiveHighTV.setText(String.valueOf(resultHigh)+(char) 0x00B0);

        String lowValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getLow();
        int lowF=Integer.parseInt(lowValue);
        int resultLow = convertFahToCel(lowF);
        dayFiveLowTV.setText(String.valueOf(resultLow)+(char) 0x00B0);

        if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getText().equals("Sunny")) {
            dayFiveIV.setImageResource(R.drawable.sunny_logo);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getText().equals("Mostly Sunny")) {
            dayFiveIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getText().equals("Partly Sunny")) {
            dayFiveIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getText().equals("Clear")) {
            dayFiveIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getText().equals("Mostly Clear")) {
            dayFiveIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getText().equals("Partly Clear")) {
            dayFiveIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getText().equals("Cloudy")) {
            dayFiveIV.setImageResource(R.drawable.cloud_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getText().equals("Partly Cloudy")) {
            dayFiveIV.setImageResource(R.drawable.clear_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(4).getText().equals("Mostly Cloudy")) {
            dayFiveIV.setImageResource(R.drawable.cloud_logo);
        }

    }public void DaySix(){
        String dayOne = weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getDay();
        String dayOneName = getDay(dayOne);
        daySixTV.setText(dayOneName);
        //dayOneHighTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh()+ (char) 0x00B0);
        // dayOneLowTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow()+ (char) 0x00B0);

        String highValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getHigh();
        int highF=Integer.parseInt(highValue);
        int resultHigh = convertFahToCel(highF);
        daySixHighTV.setText(String.valueOf(resultHigh)+(char) 0x00B0);

        String lowValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getLow();
        int lowF=Integer.parseInt(lowValue);
        int resultLow = convertFahToCel(lowF);
        daySixLowTV.setText(String.valueOf(resultLow)+(char) 0x00B0);

        if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getText().equals("Sunny")) {
            daySixIV.setImageResource(R.drawable.sunny_logo);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getText().equals("Mostly Sunny")) {
            daySixIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getText().equals("Partly Sunny")) {
            daySixIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getText().equals("Clear")) {
            daySixIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getText().equals("Mostly Clear")) {
            daySixIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getText().equals("Partly Clear")) {
            daySixIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getText().equals("Cloudy")) {
            daySixIV.setImageResource(R.drawable.cloud_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getText().equals("Partly Cloudy")) {
            daySixIV.setImageResource(R.drawable.clear_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(5).getText().equals("Mostly Cloudy")) {
            daySixIV.setImageResource(R.drawable.cloud_logo);
        }

    }
    public void DaySeven(){
        String dayOne = weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getDay();
        String dayOneName = getDay(dayOne);
        daySevenTV.setText(dayOneName);
        //dayOneHighTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh()+ (char) 0x00B0);
        // dayOneLowTV.setText(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow()+ (char) 0x00B0);

        String highValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getHigh();
        int highF=Integer.parseInt(highValue);
        int resultHigh = convertFahToCel(highF);
        daySevenHighTV.setText(String.valueOf(resultHigh)+(char) 0x00B0);

        String lowValue=weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getLow();
        int lowF=Integer.parseInt(lowValue);
        int resultLow = convertFahToCel(lowF);
        daySevenLowTV.setText(String.valueOf(resultLow)+(char) 0x00B0);

        if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getText().equals("Sunny")) {
            daySevenIV.setImageResource(R.drawable.sunny_logo);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getText().equals("Mostly Sunny")) {
            daySevenIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getText().equals("Partly Sunny")) {
            daySevenIV.setImageResource(R.drawable.sunny_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getText().equals("Clear")) {
            daySevenIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getText().equals("Mostly Clear")) {
            daySevenIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getText().equals("Partly Clear")) {
            daySevenIV.setImageResource(R.drawable.clear_moon);
        }else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getText().equals("Cloudy")) {
            daySevenIV.setImageResource(R.drawable.cloud_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getText().equals("Partly Cloudy")) {
            daySevenIV.setImageResource(R.drawable.clear_logo);
        } else if (weather.getQuery().getResults().getChannel().getItem().getForecast().get(6).getText().equals("Mostly Cloudy")) {
            daySevenIV.setImageResource(R.drawable.cloud_logo);
        }

    }

    private int convertFahToCel(int fah){
        int celsius=0;
        celsius = ((fah-32)*5)/9;
        return celsius;
    }

    public String getDay(String day){
        String dayName="";
        if (day.equals("Fri")){
            dayName="Friday";
        }else if (day.equals("Sat")){
            dayName="Saturday";
        }else if (day.equals("Sun")){
            dayName="Sunday";
        }else if (day.equals("Mon")){
            dayName="Monday";
        }else if (day.equals("Tue")){
            dayName="Tuesday";
        }else if (day.equals("Wed")){
            dayName="Wednesday";
        }else if (day.equals("Thu")){
            dayName="Thursday";
        }
        return  dayName;
    }
    //--------------------------------------------Forecast End----------------------------------------------------------//

    public void getWeatherDetails(){
        humidityTV.setText(weather.getQuery().getResults().getChannel().getAtmosphere().getHumidity()+ "%");
        String getVisi=(weather.getQuery().getResults().getChannel().getAtmosphere().getVisibility());
        double vi = Double.parseDouble(getVisi);
        double mul=0.621371;
        double miles= (vi * mul);
        Double m = new Double(miles);
        int mi = m.intValue();
        visibilityTV.setText( String.valueOf( mi + " mi" ) );

        String getPress=(weather.getQuery().getResults().getChannel().getAtmosphere().getPressure());
        double p = Double.parseDouble(getPress);
        Double pp = new Double(p);
        int press = pp.intValue();
        pressureTV.setText( String.valueOf( press + " hPa" ) );

        sunRiseTV.setText(weather.getQuery().getResults().getChannel().getAstronomy().getSunrise());
        sunSetTV.setText(weather.getQuery().getResults().getChannel().getAstronomy().getSunset());

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
            Intent logoutIntent=new Intent(WeatherActivity.this,LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        }
        if(item.getItemId() == R.id.aboutUsMenu) {
            Intent intent=new Intent(WeatherActivity.this,AboutUsActivity.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.homeMenu){
            Intent homeIntent=new Intent(WeatherActivity.this,ViewAllEventActivity.class);
            startActivity(homeIntent);
        }
        return true;

    }

}
