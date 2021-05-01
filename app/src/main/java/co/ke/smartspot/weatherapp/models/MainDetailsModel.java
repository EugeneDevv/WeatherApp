package co.ke.smartspot.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class MainDetailsModel {
    @SerializedName("temp")
    private float temp;

    @SerializedName("pressure")
    private float pressure;

    @SerializedName("humidity")
    private float humidity;

    public float getTemp() {
        return temp;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }
}
