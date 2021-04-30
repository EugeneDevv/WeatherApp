package co.ke.smartspot.weatherapp.models;

import com.google.gson.annotations.SerializedName;

public class WeatherModel {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
