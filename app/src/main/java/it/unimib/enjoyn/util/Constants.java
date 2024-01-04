package it.unimib.enjoyn.util;

public class Constants {

    public static final String SHARED_PREFERENCES_FILE_NAME = "it.unimib.enjoyn.preferences";
    public static final String SHARED_PREFERENCES_TAGS_OF_INTEREST = "tags_of_interest";

    // Constants for EncryptedSharedPreferences
    public static final String ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "it.unimib.enjoyn.encrypted_preferences";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String PASSWORD = "password";

    public static final String EVENTS_DATABASE_NAME = "events_db";

    public static final String METEO_API_BASE_URL = "https://api.open-meteo.com/v1/";

    //https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&minutely_15=temperature_2m,weather_code&forecast_days=16
    public static final String METEO_ENDPOINT = "forecast";

    public static final String METEO_LATITUDE_PARAMETER = "latitude";

    public static final String METEO_LONGITUDE_PARAMETER = "longitude";

    public static final String METEO_FORECAST_DAYS_PARAMETER = "forecast_days";

    public static final String METEO_INTERVAL_PARAMETER = "minutely_15";

    public static final int METEO_FORECAST_DAYS_VALUE = 16;

    public static final String METEO_INTERVAL_VALUE = "temperature_2m,weather_code";

    public static final String API_ERROR = "error on API fetching";
    public static final String RETROFIT_ERROR = "retrofit_error";
    // Constants for encrypted files
    public static final String ENCRYPTED_DATA_FILE_NAME = "it.unimib.enjoyn.encrypted_file.txt";

    public static final String UNEXPECTED_ERROR = "unexpected error";
}
