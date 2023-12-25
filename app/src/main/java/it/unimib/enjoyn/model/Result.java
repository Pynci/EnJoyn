package it.unimib.enjoyn.model;

public class Result {
    private Result() {}

    public boolean isSuccess() {
        return this instanceof Success;
    }

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class Success extends Result {
        private final EventsDatabaseResponse eventResponse;
        public Success(EventsDatabaseResponse eventResponse) {
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
    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
