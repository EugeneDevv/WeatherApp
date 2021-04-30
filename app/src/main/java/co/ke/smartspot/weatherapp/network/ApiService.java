package co.ke.smartspot.weatherapp.network;

import co.ke.smartspot.weatherapp.responses.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

//    String latitude = "-1.362863";
//    String longitude = "36.834583";
//    String API_KEY = "68ea9cffda56da62a2f7abfa4f88fd14";
//    @GET("lat="+latitude+"&lon="+longitude+"&appid="+API_KEY)
    @GET("weather")
//    Call<WeatherResponse> getCurrentWeatherData(@Query("lat="+latitude+"&lon="+longitude+"&appid="+API_KEY))
    Call<WeatherResponse> getCurrentWeatherData(@Query("lat") String latitude,
                                         @Query("lon") String longitude,
                                         @Query("appid") String API_KEY);

    @GET("weather")
    Call<WeatherResponse> getCityWeatherData(@Query("q") String city,
                                         @Query("appid") String API_KEY);
}
