package it.unimib.enjoyn.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Meteo;
import it.unimib.enjoyn.model.MeteoApiResponse;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.source.BaseWeatherRemoteDataSource;
import it.unimib.enjoyn.util.MeteoCallback;

public class MeteoRepository implements IMeteoRepository, MeteoCallback{

    private final MutableLiveData<Result> weatherMutableLiveData;

    private final BaseWeatherRemoteDataSource weatherRemoteDataSource;

    public MeteoRepository(BaseWeatherRemoteDataSource weatherRemoteDataSource){
        this.weatherMutableLiveData = new MutableLiveData<>();
        this.weatherRemoteDataSource = weatherRemoteDataSource;
        weatherRemoteDataSource.setMeteoCallback(this);
    }


    @Override
    public MutableLiveData<Result> fetchMeteo(String latitude, String longitude) {
        Log.d("API meteo", "dentro fetchMeteo su repository");
        weatherRemoteDataSource.getWeather(latitude, longitude);

        return weatherMutableLiveData;
    }

    @Override
    public void updateMeteo(Meteo meteo) {

    }

    @Override
    public void onSuccessFromRemote(MeteoApiResponse weatherApiResponse) {
        if (weatherMutableLiveData.getValue() != null && weatherMutableLiveData.getValue().isSuccess()) {
            Meteo meteo = ((Result.WeatherSuccess)weatherMutableLiveData.getValue()).getData().getWeather();

            Result.WeatherSuccess result = new Result.WeatherSuccess(weatherApiResponse);
            weatherMutableLiveData.postValue(result);
        } else {
            Result.WeatherSuccess result = new Result.WeatherSuccess(weatherApiResponse);
            weatherMutableLiveData.postValue(result);
        }
    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }
}
