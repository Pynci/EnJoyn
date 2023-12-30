package it.unimib.enjoyn.source;

import static it.unimib.enjoyn.util.Constants.API_ERROR;
import static it.unimib.enjoyn.util.Constants.METEO_FORECAST_DAYS_VALUE;
import static it.unimib.enjoyn.util.Constants.RETROFIT_ERROR;

import android.util.Log;

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
        Log.d("API meteo", "dentro getWeather su data source, prima chiamata");
        Call<MeteoApiResponse> weatherResponseCall = weatherApiService.getWeather(latitude, longitude, "temperature_2m", "weather_code", METEO_FORECAST_DAYS_VALUE);
        Log.d("API meteo", "dentro getWeather su data source, dopo chiamata");
        weatherResponseCall.enqueue(new Callback<MeteoApiResponse>() {
            @Override
            public void onResponse(Call<MeteoApiResponse> call, Response<MeteoApiResponse> response) {
                Log.d("API meteo", "dentro getWeather su data source, dentro OnResponse");
                if(response.body() != null && response.isSuccessful()){
                    meteoCallback.onSuccessFromRemote(response.body());
                    Log.d("API meteo", "andata a buon fine su DataSource");
                } else {
                    meteoCallback.onFailureFromRemote(new Exception(API_ERROR));
                    Log.d("API meteo", "errore su dataSource");
                }
            }

            @Override
            public void onFailure(Call<MeteoApiResponse> call, Throwable t) {
                Log.d("API meteo", "dentro getWeather su data source, dentro onFailure");
                meteoCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }
        });
    }

}
