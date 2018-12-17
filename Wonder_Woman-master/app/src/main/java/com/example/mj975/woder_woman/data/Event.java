package com.example.mj975.woder_woman.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Event implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("src")
    private String src;
    @SerializedName("href")
    private String href;

    protected Event(Parcel in) {
        title = in.readString();
        src = in.readString();
        href = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getSrc() {
        return src;
    }

    public String getHref() {
        return href;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", src='" + src + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
