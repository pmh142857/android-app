package com.pmh.iotblog.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TempIOT {

    @SerializedName("temp")
    @Expose
    private String temp;
    @SerializedName("hum")
    @Expose
    private String hum;
    @SerializedName("hc")
    @Expose
    private String hcSr501;
    @SerializedName("led")
    @Expose
    private String led;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getHcSr501() {
        return hcSr501;
    }

    public void setHcSr501(String hcSr501) {
        this.hcSr501 = hcSr501;
    }

    public String getLed() {
        return led;
    }

    public void setLed(String led) {
        this.led = led;
    }

}