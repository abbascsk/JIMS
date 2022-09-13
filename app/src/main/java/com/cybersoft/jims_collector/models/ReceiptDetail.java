package com.cybersoft.jims_collector.models;

import com.google.gson.annotations.SerializedName;

public class ReceiptDetail {

    @SerializedName("event_id")
    int event_id;
    @SerializedName("event_name")
    String event_name;
    @SerializedName("amt")
    double amt;

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }
}
