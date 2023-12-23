package it.unimib.enjoyn.repository;

import static it.unimib.enjoyn.util.Constants.METEO_FORECAST_DAYS_VALUE;
import static it.unimib.enjoyn.util.Constants.METEO_INTERVAL_VALUE;

import android.app.Application;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Meteo;
import it.unimib.enjoyn.model.MeteoApiResponse;
import it.unimib.enjoyn.service.MeteoApiService;
import it.unimib.enjoyn.util.ResponseCallback;
import it.unimib.enjoyn.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeteoRepository implements IMeteoRepository{

    private final Application application;
    private final MeteoApiService meteoApiService;
    private final ResponseCallback responseCallback;

    public MeteoRepository(Application application, MeteoApiService meteoApiService, ResponseCallback responseCallback) {
        this.application = application;
        this.meteoApiService = ServiceLocator.getInstance().getMeteoApiService();
        this.responseCallback = responseCallback;
    }

    @Override
    public void fetchMeteo(String latitude, String longitude) {
        Call<MeteoApiResponse> meteoApiResponseCall = meteoApiService.getEvents(latitude, longitude, METEO_INTERVAL_VALUE, METEO_FORECAST_DAYS_VALUE);

        meteoApiResponseCall.enqueue(new Callback<MeteoApiResponse>() {
            @Override
            public void onResponse(Call<MeteoApiResponse> call, Response<MeteoApiResponse> response) {

                if (response.body()!=null && response.isSuccessful()){
                    List<Meteo> meteoList = response.body().getMeteo();
                } else {
                    responseCallback.onFailure(application.getString(R.string.error_retriving_weather));
                }
            }

            @Override
            public void onFailure(Call<MeteoApiResponse> call, Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void updateMeteo(Meteo meteo) {

    }
}
