package co.ke.smartspot.weatherapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import co.ke.smartspot.weatherapp.network.ApiClient;
import co.ke.smartspot.weatherapp.network.ApiService;
import co.ke.smartspot.weatherapp.responses.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class CityRepository {
    private ApiService apiService;

    public CityRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }
    public LiveData<WeatherResponse> getCityWeatherData(String city, String API_KEY){
        MutableLiveData<WeatherResponse> data = new MutableLiveData<>();
        apiService.getCityWeatherData(city, API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
//                data.setValue(null);
            }
        });
        return data;
    }
}
