package it.unimib.enjoyn.source;

import static it.unimib.enjoyn.util.Constants.API_ERROR;
import static it.unimib.enjoyn.util.Constants.METEO_FORECAST_DAYS_VALUE;
import static it.unimib.enjoyn.util.Constants.RETROFIT_ERROR;

import android.util.Log;

import org.checkerframework.checker.nullness.qual.NonNull;

import it.unimib.enjoyn.model.MeteoApiResponse;
import it.unimib.enjoyn.service.MeteoApiService;
import it.unimib.enjoyn.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRemoteDataSource extends BaseWeatherRemoteDataSource{

    private final MeteoApiService weatherApiService;

    public WeatherRemoteDataSource() {
        this.weatherApiService = ServiceLocator.getInstance().getMeteoApiService();
    }


    @Override
    public void getWeather(String latitude, String longitude) {
        Call<MeteoApiResponse> weatherResponseCall = weatherApiService.getWeather(latitude, longitude, "temperature_2m", "weather_code", METEO_FORECAST_DAYS_VALUE);
        weatherResponseCall.enqueue(new Callback<MeteoApiResponse>() {
            @Override
            public void onResponse(Call<MeteoApiResponse> call, Response<MeteoApiResponse> response) {
                if(response.body() != null && response.isSuccessful()){
                    meteoCallback.onSuccessFromRemote(response.body());
                } else {
                    meteoCallback.onFailureFromRemote(new Exception(API_ERROR));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MeteoApiResponse> call, @NonNull Throwable t) {
                Log.d("ERRORE",  t.getLocalizedMessage(), t);
                meteoCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }
        });
    }

}
