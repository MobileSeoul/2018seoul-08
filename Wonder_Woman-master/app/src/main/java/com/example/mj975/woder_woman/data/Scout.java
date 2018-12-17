package com.example.mj975.woder_woman.data;

import com.google.gson.annotations.SerializedName;

public class Scout {
    @SerializedName("loc_name")
    private String name;
    @SerializedName("number")
    private String number;

    public Scout(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
