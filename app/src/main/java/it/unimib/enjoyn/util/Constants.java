package it.unimib.enjoyn.util;

public class Constants {

    public static final String SHARED_PREFERENCES_FILE_NAME = "it.unimib.enjoyn.preferences";
    public static final String SHARED_PREFERENCES_TAGS_OF_INTEREST = "tags_of_interest";

    // Constants for EncryptedSharedPreferences
    public static final String ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "it.unimib.enjoyn.encrypted_preferences";
    //public static final String EMAIL_ADDRESS = "email_address";
    //public static final String PASSWORD = "password";

    public static final String EVENTS_DATABASE_NAME = "events_db";

    // Constants for encrypted files
    public static final String ENCRYPTED_DATA_FILE_NAME = "it.unimib.enjoyn.encrypted_file.txt";

    // Costants for paths in the firebase DB
    public static final String DATABASE_PATH = "https://enjoyn-9adca-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String STORAGE_PATH = "gs://enjoyn-9adca.appspot.com";
    public static final String USERS_PATH = "Users";
    public static final String CATEGORIES_PATH = "Categories";
    public static final String INTERESTS_PATH = "Interests";
    public static final String EVENTS_PATH = "Events";
    public static final String EVENTCREATIONS_PATH = "EventCreations";
    public static final String PARTICIPATIONS_PATH = "EventParticipations";

    public static final String WEATHER_API_BASE_URL = "https://api.open-meteo.com/v1/";

    //https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&minutely_15=temperature_2m,weather_code&forecast_days=16
    public static final String WEATHER_ENDPOINT = "forecast";

    public static final String WEATHER_LATITUDE_PARAMETER = "latitude";

    public static final String WEATHER_LONGITUDE_PARAMETER = "longitude";

    public static final String WEATHER_FORECAST_DAYS_PARAMETER = "forecast_days";

    public static final String WEATHER_INTERVAL_PARAMETER = "minutely_15";

    public static final int WEATHER_FORECAST_DAYS_VALUE = 16;

    public static final String WEATHER_INTERVAL_VALUE = "temperature_2m,weather_code";

    public static final String API_ERROR = "error on API fetching";
    public static final String RETROFIT_ERROR = "retrofit_error";
    public static final String EMPTY_LOCATION = "empty_location";
    public static final String EMPTY_FIELDS = "empty fields";
    public static final String VIEW_MODEL_ERROR ="view model";
    // Constants for encrypted files

    public static final String UNEXPECTED_ERROR = "unexpected error";
    public static final String DATE_BEFORE_ERROR = "Riprova, hai scelto una data passata";
    public static final String TIME_BEFORE_DATE_ERROR = "Scegli prima la data";
    public static final String STATE_LOCATION = "location";

    // errori personalizzati
    public static final String USER_ALREADY_LOGGED_ERROR = "USER_ALREADY_LOGGED";
    public static final String USER_NOT_LOGGED_ERROR = "USER_NOT_LOGGED";
    public static final String SIGNUP_ERROR = "SIGNUP_FAILED";
    public static final String SIGNIN_ERROR = "SIGNIN_FAILED";
    public static final String SESSION_REFRESH_ERROR = "SESSION_REFRESH_FAILED";
    public static final String USER_LOCAL_FETCH_ERROR = "USER_LOCAL_FETCH_FAILED";
    public static final String USER_LOCAL_UPDATE_ERROR = "USER_LOCAL_UPDATE_FAILED";
    public static final String USER_LOCAL_CREATION_ERROR = "USER_LOCAL_CREATION_FAILED";
    public static final String USER_LOCAL_DELETION_ERROR = "USER_LOCAL_DELETION_FAILED";
    public static final String USER_REMOTE_FETCH_ERROR = "USER_REMOTE_FETCH_FAILED";
    public static final String USER_REMOTE_UPDATE_ERROR = "USER_REMOTE_UPDATE_FAILED";
    public static final String EMAIL_SENDING_ERROR = "EMAIL_SENDING_FAILED";
    public static final String EMAIL_RESET_PASSWORD_SENDING_ERROR = "EMAIL_RESET_PASSWORD_SENDING_ERROR";
    public static final String INTEREST_LOCAL_FETCH_ERROR = "INTEREST_LOCAL_FETCH_FAILED";
    public static final String INTEREST_LOCAL_CREATION_ERROR = "INTEREST_LOCAL_CREATION_FAILED";
    public static final String INTEREST_LOCAL_DELETION_ERROR = "INTEREST_LOCAL_DELETION_FAILED";
    public static final String INTEREST_REMOTE_FETCH_ERROR = "INTEREST_REMOTE_FETCH_ERROR";
    public static final String INTEREST_REMOTE_CREATION_ERROR = "INTEREST_REMOTE_CREATION_ERROR";
    public static final String EVENT_REMOTE_CREATION_ERROR = "EVENT_REMOTE_CREATION_FAILED";
    public static final String EVENT_REMOTE_FETCH_ERROR = "EVENT_REMOTE_FETCH_FAILED";
    public static final String EVENT_REMOTE_UPDATE_ERROR = "EVENT_REMOTE_UPDATE_FAILED";
    public static final String PARTICIPATION_REMOTE_CREATION_ERROR = "PARTICIPATION_REMOTE_CREATION_FAILED";
    public static final String PARTICIPATION_REMOTE_FETCH_ERROR = "PARTICIPATION_REMOTE_FETCH_FAILED";
    public static final String PARTICIPATION_REMOTE_DELETION_ERROR = "PARTICIPATION_REMOTE_DELETION_FAILED";
}
