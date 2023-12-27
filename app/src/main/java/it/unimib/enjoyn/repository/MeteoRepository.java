package it.unimib.enjoyn.repository;

import static it.unimib.enjoyn.util.Constants.METEO_FORECAST_DAYS_PARAMETER;
import static it.unimib.enjoyn.util.Constants.METEO_FORECAST_DAYS_VALUE;
import static it.unimib.enjoyn.util.Constants.METEO_INTERVAL_VALUE;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Meteo;
import it.unimib.enjoyn.model.MeteoApiResponse;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.service.MeteoApiService;
import it.unimib.enjoyn.source.BaseWeatherRemoteDataSource;
import it.unimib.enjoyn.util.MeteoCallback;
import it.unimib.enjoyn.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeteoRepository implements IMeteoRepository, MeteoCallback{

    private final MutableLiveData<Result> weatherMutableLiveData;

    private final BaseWeatherRemoteDataSource weatherRemoteDataSource;

    public MeteoRepository(BaseWeatherRemoteDataSource weatherRemoteDataSource){
        this.weatherMutableLiveData = new MutableLiveData<>();
        this.weatherRemoteDataSource = weatherRemoteDataSource;
        weatherRemoteDataSource.setMeteoCallback(this);
    }


    @Override
    public MutableLiveData<Result> fetchMeteo(String latitude, String longitude) {
        weatherRemoteDataSource.getWeather(latitude, longitude, METEO_FORECAST_DAYS_VALUE, METEO_INTERVAL_VALUE);

        return weatherMutableLiveData;
    }

    @Override
    public void updateMeteo(Meteo meteo) {

    }

    @Override
    public void onSuccessFromRemote(MeteoApiResponse weatherApiResponse) {

    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }
}
