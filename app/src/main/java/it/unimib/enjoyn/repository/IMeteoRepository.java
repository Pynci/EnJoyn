package it.unimib.enjoyn.repository;

import static it.unimib.enjoyn.util.Constants.METEO_INTERVAL_PARAMETER;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Meteo;
import it.unimib.enjoyn.model.Result;
import retrofit2.http.Query;

public interface IMeteoRepository {

    MutableLiveData<Result> fetchMeteo(String latitude, String longitude);

    void updateMeteo(Meteo meteo);
}
