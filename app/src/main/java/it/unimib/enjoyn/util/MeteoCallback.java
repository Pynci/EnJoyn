package it.unimib.enjoyn.util;

import java.util.List;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Meteo;

public interface MeteoCallback {

    void onSuccess(List<Meteo> meteoList, long lastUpdate);
    void onFailure(String errorMessage);
}
