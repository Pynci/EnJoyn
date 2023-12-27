package it.unimib.enjoyn.util;

import android.app.Application;

import it.unimib.enjoyn.database.EventsRoomDatabase;
import it.unimib.enjoyn.repository.IMeteoRepository;
import it.unimib.enjoyn.repository.MeteoRepository;
import it.unimib.enjoyn.service.MeteoApiService;
import it.unimib.enjoyn.source.BaseWeatherRemoteDataSource;
import it.unimib.enjoyn.source.WeatherRemoteDataSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    public MeteoApiService getMeteoApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.METEO_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(MeteoApiService.class);
    }

    public EventsRoomDatabase getEventDao(Application application) { //istanza di news room database
        return EventsRoomDatabase.getDatabase(application);
    }

    public IMeteoRepository getWeatherRepository(Application application){
        BaseWeatherRemoteDataSource weatherRemoteDataSource;

        weatherRemoteDataSource = new WeatherRemoteDataSource();

        return new MeteoRepository(weatherRemoteDataSource);
    }
}
