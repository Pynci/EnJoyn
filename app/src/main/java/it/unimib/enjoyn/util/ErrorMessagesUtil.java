package it.unimib.enjoyn.util;

import android.app.Application;

import static it.unimib.enjoyn.util.Constants.API_ERROR;
import static it.unimib.enjoyn.util.Constants.RETROFIT_ERROR;

public class ErrorMessagesUtil {

    private Application application;

    public ErrorMessagesUtil(Application application){
        this.application = application;
    }

    public String getErrorMessage(String error){
        switch (error){
            case RETROFIT_ERROR:
                return "Error recovering weather";
            case API_ERROR:
                return "Error on API";
            default:
                return "Unexpected error";
        }
    }
}
