package com.example.mj975.woder_woman.service;

import com.example.mj975.woder_woman.data.Delivery;
import com.example.mj975.woder_woman.data.Event;
import com.example.mj975.woder_woman.data.SafeHouse;
import com.example.mj975.woder_woman.data.Scout;
import com.example.mj975.woder_woman.data.Toilet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DatabaseAPI {
    @GET("event/all")
    Call<ArrayList<Event>> getAllEvents();

    @GET("toilet/all")
    Call<ArrayList<Toilet>> getAllToilets();

    @GET("toilet/near")
    Call<ArrayList<Toilet>> getNearToilets(@Query("lat") float lat, @Query("lng") float lng);

    @GET("delivery/all")
    Call<ArrayList<Delivery>> getAllDelivery();

    @GET("delivery/search")
    Call<ArrayList<Delivery>> getDeliveryList(@Query("addr") String query);

    @GET("safehouse/all")
    Call<ArrayList<SafeHouse>> getAllSafeHouse();

    @GET("scout/all")
    Call<ArrayList<Scout>> getAllScout();


}
