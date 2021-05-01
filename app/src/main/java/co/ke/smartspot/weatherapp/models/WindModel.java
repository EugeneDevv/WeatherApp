package co.ke.smartspot.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class WindModel {
    @SerializedName("speed")
    private float speed;

    public float getSpeed() {
        return speed;
    }
}
