package it.unimib.enjoyn.util;

import android.app.Application;

import static it.unimib.enjoyn.util.Constants.API_ERROR;
import static it.unimib.enjoyn.util.Constants.EMPTY_FIELDS;
import static it.unimib.enjoyn.util.Constants.EMPTY_LOCATION;
import static it.unimib.enjoyn.util.Constants.RETROFIT_ERROR;

public class ErrorMessagesUtil {

    private Application application;

    public ErrorMessagesUtil(Application application){
        this.application = application;
    }

    public String getWeatherErrorMessage(String error){
        switch (error){
            case RETROFIT_ERROR:
                return "Error recovering weather";
            case API_ERROR:
                return "Error on API";
            default:
                return "Unexpected error";
        }
    }

    public String getMapErrorMessage(String error){
        switch (error){
            case EMPTY_LOCATION:
                return "Location missing";
            default:
                return "Unexpected error";
        }
    }

    public String getNewEventErrorMessage(String error){
        switch (error){
            case EMPTY_FIELDS:
                return "Some fields must be empty";
            default:
                return "Unexpected error";
        }
    }

}
