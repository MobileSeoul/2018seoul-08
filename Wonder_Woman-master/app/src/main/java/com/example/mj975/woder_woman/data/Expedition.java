package com.example.mj975.woder_woman.data;

import java.io.Serializable;
import java.util.List;

public class Expedition implements Serializable {
    private String email;
    private String location;
    private String time;
    private boolean isEnd;
    private List<String> party;

    public Expedition() {

    }

    public Expedition(String email, String location, String time, boolean isEnd, List<String> party) {
        this.email = email;
        this.location = location;
        this.time = time;
        this.isEnd = isEnd;
        this.party = party;
    }

    public List<String> getParty() {
        return party;
    }

    public void setParty(List<String> party) {
        this.party = party;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
}
