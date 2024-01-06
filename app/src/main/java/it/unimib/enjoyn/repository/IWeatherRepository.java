package it.unimib.enjoyn.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Weather;
import it.unimib.enjoyn.model.Result;

public interface IWeatherRepository {

    MutableLiveData<Result> fetchWeather(String latitude, String longitude);

    void updateWeather(Weather weather);
}
