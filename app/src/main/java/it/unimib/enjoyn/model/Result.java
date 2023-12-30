package it.unimib.enjoyn.model;

import android.util.Log;

public abstract class Result {

    private Result() {}

    public boolean isSuccessful(){
        Log.d("API meteo", "dentro isSuccessful su Result");
        return this instanceof Success;
    }

    public boolean isSuccess() {
        return this instanceof Success;
    }

    public static final class Success extends Result{

        private final MeteoResponse meteoResponse;

        public Success(MeteoResponse meteoResponse) {
            this.meteoResponse = meteoResponse;
        }
        public MeteoResponse getData(){
            return meteoResponse;
        }
    }

    public static final class Error extends Result{
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }
}
