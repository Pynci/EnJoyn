package it.unimib.enjoyn.util;

import java.util.List;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Meteo;
import it.unimib.enjoyn.model.MeteoApiResponse;

public interface MeteoCallback {

    void onSuccessFromRemote(MeteoApiResponse weatherApiResponse);

    void onFailureFromRemote(Exception exception);
}
