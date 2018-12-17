package com.example.mj975.woder_woman.data;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import net.sharewire.googlemapsclustering.ClusterItem;

public class SafeHouse implements ClusterItem {
    @SerializedName("loc_name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;

    public SafeHouse(String name, String address, String longitude, String latitude) {
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public double getLatitude() {
        return Double.valueOf(latitude);
    }

    @Override
    public double getLongitude() {
        return Double.valueOf(longitude);
    }

    @Nullable
    @Override
    public String getTitle() {
        return this.name;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return this.address;
    }
}
