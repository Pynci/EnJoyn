package it.unimib.enjoyn.service;

import static it.unimib.enjoyn.util.Constants.WEATHER_ENDPOINT;
import static it.unimib.enjoyn.util.Constants.WEATHER_FORECAST_DAYS_PARAMETER;
import static it.unimib.enjoyn.util.Constants.WEATHER_INTERVAL_PARAMETER;
import static it.unimib.enjoyn.util.Constants.WEATHER_LATITUDE_PARAMETER;
import static it.unimib.enjoyn.util.Constants.WEATHER_LONGITUDE_PARAMETER;

import it.unimib.enjoyn.model.WeatherApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET(WEATHER_ENDPOINT)
    Call<WeatherApiResponse> getWeather(
                @Query(WEATHER_LATITUDE_PARAMETER) String latitude,
                @Query(WEATHER_LONGITUDE_PARAMETER) String longitude,
                @Query(WEATHER_INTERVAL_PARAMETER) String interval,
                @Query(WEATHER_INTERVAL_PARAMETER) String interval2,
                @Query(WEATHER_FORECAST_DAYS_PARAMETER) int forecastDays);

}
