package it.unimib.enjoyn.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Weather;
import it.unimib.enjoyn.model.WeatherApiResponse;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.source.BaseWeatherRemoteDataSource;
import it.unimib.enjoyn.util.WeatherCallback;

public class WeatherRepository implements IWeatherRepository, WeatherCallback {

    private final MutableLiveData<Result> weatherMutableLiveData;

    private final BaseWeatherRemoteDataSource weatherRemoteDataSource;

    public WeatherRepository(BaseWeatherRemoteDataSource weatherRemoteDataSource){
        this.weatherMutableLiveData = new MutableLiveData<>();
        this.weatherRemoteDataSource = weatherRemoteDataSource;
        weatherRemoteDataSource.setMeteoCallback(this);
    }


    @Override
    public MutableLiveData<Result> fetchWeather(String latitude, String longitude) {
        weatherRemoteDataSource.getWeather(latitude, longitude);

        return weatherMutableLiveData;
    }

    @Override
    public void updateWeather(Weather weather) {

    }

    @Override
    public void onSuccessFromRemote(WeatherApiResponse weatherApiResponse) {
        Result.WeatherSuccess result = new Result.WeatherSuccess(weatherApiResponse);
        weatherMutableLiveData.postValue(result);

    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.WeatherError result= new Result.WeatherError(exception.getMessage());
        weatherMutableLiveData.postValue(result);
    }
}
