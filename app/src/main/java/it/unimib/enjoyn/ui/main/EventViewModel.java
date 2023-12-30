package it.unimib.enjoyn.ui.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.IMeteoRepository;

public class EventViewModel extends ViewModel {

    private final IMeteoRepository weatherRepository;

    private MutableLiveData<Result> weatherListLiveData;

    public EventViewModel(IMeteoRepository iWeatherRepository) {
        this.weatherRepository = iWeatherRepository;
    }

    public MutableLiveData<Result> getWeather(String latitude, String logitude){
        Log.d("API meteo", "dentro getWeather su viewModel");
        if (weatherListLiveData == null){
            fetchWeather(latitude, logitude);
        }
        return weatherListLiveData;
    }

    private void fetchWeather(String latitude, String longitude){
        Log.d("API meteo", "dentro fetchWeather su viewModel");
        weatherListLiveData = weatherRepository.fetchMeteo(latitude, longitude);
    }
}
