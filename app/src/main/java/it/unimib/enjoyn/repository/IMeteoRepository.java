package it.unimib.enjoyn.repository;

import static it.unimib.enjoyn.util.Constants.METEO_INTERVAL_PARAMETER;

import it.unimib.enjoyn.model.Meteo;
import retrofit2.http.Query;

public interface IMeteoRepository {

    void fetchMeteo(String latitude, String longitude);

    void updateMeteo(Meteo meteo);
}
