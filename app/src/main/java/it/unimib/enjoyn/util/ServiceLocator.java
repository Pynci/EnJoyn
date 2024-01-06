package it.unimib.enjoyn.util;

import android.app.Application;

import it.unimib.enjoyn.database.EventsRoomDatabase;
import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.WeatherRepository;
import it.unimib.enjoyn.service.WeatherApiService;
import it.unimib.enjoyn.source.BaseWeatherRemoteDataSource;
import it.unimib.enjoyn.source.WeatherRemoteDataSource;
import it.unimib.enjoyn.repository.EventRepositoryWithLiveData;
import it.unimib.enjoyn.repository.IEventRepositoryWithLiveData;
import it.unimib.enjoyn.source.BaseEventLocalDataSource;
import it.unimib.enjoyn.source.BaseEventRemoteDataSource;
import it.unimib.enjoyn.source.EventLocalDataSource;
import it.unimib.enjoyn.source.EventMockRemoteDataSource;
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

    public WeatherApiService getMeteoApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.WEATHER_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(WeatherApiService.class);
    }

    public EventsRoomDatabase getEventDao(Application application) { //istanza di event room database
        return EventsRoomDatabase.getDatabase(application);
    }

    public IWeatherRepository getWeatherRepository(Application application){
        BaseWeatherRemoteDataSource weatherRemoteDataSource;

        weatherRemoteDataSource = new WeatherRemoteDataSource();

        return new WeatherRepository(weatherRemoteDataSource);
    }

    public IEventRepositoryWithLiveData getEventRepository(Application application){
        BaseEventLocalDataSource eventLocalDataSource;
        BaseEventRemoteDataSource eventRemoteDataSource;
        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);

        eventRemoteDataSource = new EventMockRemoteDataSource(jsonParserUtil);
        eventLocalDataSource = new EventLocalDataSource(getEventDao(application));

        return new EventRepositoryWithLiveData(eventLocalDataSource, eventRemoteDataSource);
    }
}
