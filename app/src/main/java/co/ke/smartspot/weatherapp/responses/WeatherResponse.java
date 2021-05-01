package co.ke.smartspot.weatherapp.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.ke.smartspot.weatherapp.models.MainDetailsModel;
import co.ke.smartspot.weatherapp.models.WeatherModel;
import co.ke.smartspot.weatherapp.models.WindModel;

public class WeatherResponse {

    @SerializedName("name")
    private String city;

    @SerializedName("weather")
    private List<WeatherModel> weatherModel;

    @SerializedName("main")
    private MainDetailsModel mainDetailsModel;

    @SerializedName("wind")
    private WindModel windModel;

    @SerializedName("visibility")
    private int visibility;

    @SerializedName("dt")
    private int dateTime;

    public int getDateTime() {
        return dateTime;
    }

    public String getCity() {
        return city;
    }

    public List<WeatherModel> getWeatherModel() {
        return weatherModel;
    }

    public WindModel getWindModel() {
        return windModel;
    }

    public int getVisibility() {
        return visibility;
    }

    public MainDetailsModel getMainDetailsModel() {
        return mainDetailsModel;
    }
}
