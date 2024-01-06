package it.unimib.enjoyn.util;

import it.unimib.enjoyn.model.WeatherApiResponse;

public interface WeatherCallback {

    void onSuccessFromRemote(WeatherApiResponse weatherApiResponse);

    void onFailureFromRemote(Exception exception);
}
