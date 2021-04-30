package co.ke.smartspot.weatherapp.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import co.ke.smartspot.weatherapp.models.TempModel;
import co.ke.smartspot.weatherapp.models.WeatherData;
import co.ke.smartspot.weatherapp.models.WeatherModel;

public class WeatherResponse {

    @SerializedName("name")
    private String city;

    @SerializedName("weather")
    private List<WeatherModel> weatherModel;

    @SerializedName("main")
    private TempModel tempModel;

    public String getCity() {
        return city;
    }

    public List<WeatherModel> getWeatherModel() {
        return weatherModel;
    }

    public TempModel getTempModel() {
        return tempModel;
    }
}
