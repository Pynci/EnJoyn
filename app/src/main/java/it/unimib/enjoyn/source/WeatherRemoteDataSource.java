package it.unimib.enjoyn.source;

import static it.unimib.enjoyn.util.Constants.API_ERROR;
import static it.unimib.enjoyn.util.Constants.RETROFIT_ERROR;

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
    public void getWeather(String latitude, String longitude, int forecastDays, String interval) {
        Call<MeteoApiResponse> weatherResponseCall = weatherApiService.getWeather(latitude, longitude, forecastDays, interval);

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
            public void onFailure(Call<MeteoApiResponse> call, Throwable t) {
                meteoCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }
        });
    }

}
