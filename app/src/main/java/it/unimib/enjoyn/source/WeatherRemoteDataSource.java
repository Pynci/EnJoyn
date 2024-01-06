package it.unimib.enjoyn.source;

import static it.unimib.enjoyn.util.Constants.API_ERROR;
import static it.unimib.enjoyn.util.Constants.WEATHER_FORECAST_DAYS_VALUE;
import static it.unimib.enjoyn.util.Constants.RETROFIT_ERROR;

import android.util.Log;

import org.checkerframework.checker.nullness.qual.NonNull;

import it.unimib.enjoyn.model.WeatherApiResponse;
import it.unimib.enjoyn.service.WeatherApiService;
import it.unimib.enjoyn.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRemoteDataSource extends BaseWeatherRemoteDataSource{

    private final WeatherApiService weatherApiService;

    public WeatherRemoteDataSource() {
        this.weatherApiService = ServiceLocator.getInstance().getMeteoApiService();
    }


    @Override
    public void getWeather(String latitude, String longitude) {
        Call<WeatherApiResponse> weatherResponseCall = weatherApiService.getWeather(latitude, longitude, "temperature_2m", "weather_code", WEATHER_FORECAST_DAYS_VALUE);
        weatherResponseCall.enqueue(new Callback<WeatherApiResponse>() {
            @Override
            public void onResponse(Call<WeatherApiResponse> call, Response<WeatherApiResponse> response) {
                if(response.body() != null && response.isSuccessful()){
                    weatherCallback.onSuccessFromRemote(response.body());
                } else {
                    weatherCallback.onFailureFromRemote(new Exception(API_ERROR));
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherApiResponse> call, @NonNull Throwable t) {
                Log.d("ERRORE",  t.getLocalizedMessage(), t);
                weatherCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }
        });
    }

}
