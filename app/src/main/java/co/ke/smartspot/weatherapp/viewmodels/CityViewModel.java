package co.ke.smartspot.weatherapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import co.ke.smartspot.weatherapp.repositories.CityRepository;
import co.ke.smartspot.weatherapp.responses.WeatherResponse;

public class CityViewModel extends ViewModel {
    private CityRepository cityRepository;

    public CityViewModel() {
        cityRepository = new  CityRepository();
    }
    public LiveData<WeatherResponse> getCityWeatherData(String city, String API_KEY){
        return cityRepository.getCityWeatherData(city, API_KEY);
    }
}
