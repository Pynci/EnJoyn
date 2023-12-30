package it.unimib.enjoyn.source;

import it.unimib.enjoyn.util.MeteoCallback;

public abstract class BaseWeatherRemoteDataSource {

    protected MeteoCallback meteoCallback;

    public void setMeteoCallback(MeteoCallback meteoCallback) {
        this.meteoCallback = meteoCallback;
    }

    public abstract void getWeather(String latitude, String longitude);
}
