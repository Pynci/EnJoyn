package it.unimib.enjoyn.source;

import it.unimib.enjoyn.util.WeatherCallback;

public abstract class BaseWeatherRemoteDataSource {

    protected WeatherCallback weatherCallback;

    public void setMeteoCallback(WeatherCallback weatherCallback) {
        this.weatherCallback = weatherCallback;
    }

    public abstract void getWeather(String latitude, String longitude);
}
