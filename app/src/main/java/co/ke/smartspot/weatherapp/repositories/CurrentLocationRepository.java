package co.ke.smartspot.weatherapp.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import co.ke.smartspot.weatherapp.network.ApiClient;
import co.ke.smartspot.weatherapp.network.ApiService;
import co.ke.smartspot.weatherapp.responses.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class CurrentLocationRepository {
    private ApiService apiService;

    public CurrentLocationRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }
    public LiveData<WeatherResponse> getCurrentWeatherData(String latitude, String longitude, String API_KEY){
        MutableLiveData<WeatherResponse> data = new MutableLiveData<>();
        apiService.getCurrentWeatherData(latitude,longitude,API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call,@NonNull Response<WeatherResponse> response) {
                data.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call,@NonNull Throwable t) {
                data.setValue(null);
//                Log.d("Throw", t.getMessage());
            }
        });
        return data;
    }
}
