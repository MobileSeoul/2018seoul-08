package com.example.mj975.woder_woman.service;

import com.example.mj975.woder_woman.data.Delivery;
import com.example.mj975.woder_woman.data.Event;
import com.example.mj975.woder_woman.data.SafeHouse;
import com.example.mj975.woder_woman.data.Scout;
import com.example.mj975.woder_woman.data.Toilet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseClient implements DatabaseAPI {
    private DatabaseAPI api;

    private DatabaseClient() {
        OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();

        Gson gson = new GsonBuilder().create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://52.199.117.247:8888/a/").build();

        api = retrofit.create(DatabaseAPI.class);
    }

    @Override
    public Call<ArrayList<Event>> getAllEvents() {
        return api.getAllEvents();
    }

    @Override
    public Call<ArrayList<Toilet>> getAllToilets() {
        return api.getAllToilets();
    }

    @Override
    public Call<ArrayList<Toilet>> getNearToilets(float lat, float lng) {
        return api.getNearToilets(lat, lng);
    }

    @Override
    public Call<ArrayList<Delivery>> getAllDelivery() {
        return api.getAllDelivery();
    }

    @Override
    public Call<ArrayList<Delivery>> getDeliveryList(String query) {
        return api.getDeliveryList(query);
    }

    @Override
    public Call<ArrayList<SafeHouse>> getAllSafeHouse() {
        return api.getAllSafeHouse();
    }

    @Override
    public Call<ArrayList<Scout>> getAllScout() {
        return api.getAllScout();
    }

    private static class SingleTon {
        private static final DatabaseClient INSTANCE = new DatabaseClient();
    }

    public static DatabaseClient getInstance() {
        return SingleTon.INSTANCE;
    }
}
