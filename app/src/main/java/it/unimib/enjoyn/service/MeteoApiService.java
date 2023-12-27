package it.unimib.enjoyn.service;

import static it.unimib.enjoyn.util.Constants.METEO_ENDPOINT;
import static it.unimib.enjoyn.util.Constants.METEO_FORECAST_DAYS_PARAMETER;
import static it.unimib.enjoyn.util.Constants.METEO_INTERVAL_PARAMETER;
import static it.unimib.enjoyn.util.Constants.METEO_LATITUDE_PARAMETER;
import static it.unimib.enjoyn.util.Constants.METEO_LONGITUDE_PARAMETER;

import it.unimib.enjoyn.model.MeteoApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MeteoApiService {

    @GET(METEO_ENDPOINT)
    Call<MeteoApiResponse> getWeather(
                @Query(METEO_LATITUDE_PARAMETER) String latitude,
                @Query(METEO_LONGITUDE_PARAMETER) String longitude,
                @Query(METEO_FORECAST_DAYS_PARAMETER) int forecastDays,
                @Query(METEO_INTERVAL_PARAMETER) String interval);

}
