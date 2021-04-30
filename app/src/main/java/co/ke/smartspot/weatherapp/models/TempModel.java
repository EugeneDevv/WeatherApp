package co.ke.smartspot.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class TempModel {
    @SerializedName("temp")
    private float temp;

    public float getTemp() {
        return temp;
    }
}
