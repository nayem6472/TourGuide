package com.nayem.tourguide.api;

import com.nayem.tourguide.model.Weather;
import com.nayem.tourguide.model.modelNearby.NearbyPlace;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherApi {
    @GET()
    Call<Weather>getWeatherData(@Url String url);

    @GET()
    Call<NearbyPlace>getNearbyPlace(@Url String url);

}
