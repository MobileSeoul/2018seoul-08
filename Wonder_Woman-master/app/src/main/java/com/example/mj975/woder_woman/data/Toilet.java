package com.example.mj975.woder_woman.data;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;


import net.sharewire.googlemapsclustering.ClusterItem;

import java.io.Serializable;




public class Toilet implements Serializable, ClusterItem {
    @SerializedName("naddress")
    private String newAddr;
    @SerializedName("oaddress")
    private String oldAddr;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("contents")
    private String content;
    @SerializedName("time")
    private String openTime;
    @SerializedName("loc_type")
    private String locType;
    @SerializedName("isdanger")
    private int isDanger;

    public String getNewAddr() {
        return newAddr;
    }

    public String getOldAddr() {
        return oldAddr;
    }

    public String getContent() {
        return content;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getLocType() {
        return locType;
    }

    public int getIsDanger() {
        return isDanger;
    }

    @Override
    public String toString() {
        return "Toilet{" +
                "newAddr='" + newAddr + '\'' +
                ", oldAddr='" + oldAddr + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", content='" + content + '\'' +
                ", openTime='" + openTime + '\'' +
                ", locType='" + locType + '\'' +
                ", isDanger=" + isDanger +
                '}';
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
        return content;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return openTime;
    }
}
