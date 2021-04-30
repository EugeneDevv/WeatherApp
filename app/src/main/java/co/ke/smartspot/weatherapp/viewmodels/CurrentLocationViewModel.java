package co.ke.smartspot.weatherapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import co.ke.smartspot.weatherapp.repositories.CurrentLocationRepository;
import co.ke.smartspot.weatherapp.responses.WeatherResponse;

public class CurrentLocationViewModel extends ViewModel {
    private CurrentLocationRepository currentLocationRepository;

    public CurrentLocationViewModel(){
        currentLocationRepository = new CurrentLocationRepository();
    }

    public LiveData<WeatherResponse> getCurrentWeatherData(String latitude, String longitude, String API_KEY){
        return currentLocationRepository.getCurrentWeatherData(latitude,longitude,API_KEY);
    }
}
