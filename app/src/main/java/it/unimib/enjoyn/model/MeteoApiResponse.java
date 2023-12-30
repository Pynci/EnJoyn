package it.unimib.enjoyn.model;

import java.util.List;

public class MeteoApiResponse extends MeteoResponse{

    double latitude;
    double longitude;
    double generationtime_ms;
    double utc_offset_seconds;
    String timezone;
    String timezone_abbreviation;
    double elevation;
    Object minutely_15_units;

    public MeteoApiResponse(Meteo meteo, double latitude, double longitude, double generationtime_ms, double utc_offset_seconds, String timezone, String timezone_abbreviation, double elevation, Object minutely_15_units) {
        super(meteo);
        this.latitude = latitude;
        this.longitude = longitude;
        this.generationtime_ms = generationtime_ms;
        this.utc_offset_seconds = utc_offset_seconds;
        this.timezone = timezone;
        this.timezone_abbreviation = timezone_abbreviation;
        this.elevation = elevation;
        this.minutely_15_units = minutely_15_units;
    }


}
