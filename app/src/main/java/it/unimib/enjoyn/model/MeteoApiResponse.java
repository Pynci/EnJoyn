package it.unimib.enjoyn.model;

import java.util.List;

public class MeteoApiResponse {

    private List<Meteo> meteo;

    public MeteoApiResponse(List<Meteo> meteo) {
        this.meteo = meteo;
    }

    public List<Meteo> getMeteo() {
        return meteo;
    }

    public void setMeteo(List<Meteo> meteo) {
        this.meteo = meteo;
    }
}
