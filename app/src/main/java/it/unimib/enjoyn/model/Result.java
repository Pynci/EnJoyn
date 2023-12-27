package it.unimib.enjoyn.model;

public abstract class Result {

    private Result() {}

    public boolean isSuccessful(){
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
