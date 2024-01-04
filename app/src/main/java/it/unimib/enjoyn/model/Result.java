package it.unimib.enjoyn.model;

public class Result {
    private Result() {
    }

    public boolean isSuccess() {
        return (this instanceof EventSuccess || this instanceof WeatherSuccess);
    }

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class EventSuccess extends Result {
        private final EventsDatabaseResponse eventResponse;

        public EventSuccess(EventsDatabaseResponse eventResponse) {
            this.eventResponse = eventResponse;
        }

        public EventsDatabaseResponse getData() {
            return eventResponse;
        }
    }

    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class EventError extends Result {
        private final String message;

        public EventError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static final class WeatherSuccess extends Result{

        private final MeteoResponse meteoResponse;

        public WeatherSuccess(MeteoResponse meteoResponse) {
            this.meteoResponse = meteoResponse;
        }
        public MeteoResponse getData(){
            return meteoResponse;
        }
    }

    public static final class WeatherError extends Result{
        private final String message;

        public WeatherError(String message) {
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }
}
