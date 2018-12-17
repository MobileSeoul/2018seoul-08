package com.example.mj975.woder_woman.data;

import com.google.gson.annotations.SerializedName;

public class Delivery {
    @SerializedName("loc_name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("delivery_num")
    private int postal;

    public Delivery(String name, String address, int postal) {
        this.name = name;
        this.address = address;
        this.postal = postal;
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

    public int getPostal() {
        return postal;
    }

    public void setPostal(int postal) {
        this.postal = postal;
    }
}
