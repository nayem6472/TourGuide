package com.nayem.tourguide.api;

import com.nayem.tourguide.activity.DirectionResponse;
import com.nayem.tourguide.model.Weather;
import com.nayem.tourguide.model.modelNearby.NearbyPlace;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NearbyApi {

    @GET()
    Call<NearbyPlace>getNearbyPlace(@Url String url);


    @GET
    Call<DirectionResponse> getDirection(@Url String url);

}
