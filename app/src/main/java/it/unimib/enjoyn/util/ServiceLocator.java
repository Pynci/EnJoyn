package it.unimib.enjoyn.util;

import android.app.Application;

import it.unimib.enjoyn.database.LocalRoomDatabase;
import it.unimib.enjoyn.repository.MapRepository;
import it.unimib.enjoyn.repository.category.CategoryRepository;
import it.unimib.enjoyn.repository.category.ICategoryRepository;
import it.unimib.enjoyn.repository.interests.IInterestRepository;
import it.unimib.enjoyn.repository.interests.InterestRepository;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.repository.user.UserRepository;
import it.unimib.enjoyn.source.MapRemoteDataSource;
import it.unimib.enjoyn.source.categories.CategoryRemoteDataSource;
import it.unimib.enjoyn.source.events.EventRemoteDataSource;
import it.unimib.enjoyn.source.interests.BaseInterestLocalDataSource;
import it.unimib.enjoyn.source.interests.BaseInterestRemoteDataSource;
import it.unimib.enjoyn.source.interests.InterestLocalDataSource;
import it.unimib.enjoyn.source.interests.InterestRemoteDataSource;
import it.unimib.enjoyn.source.users.AuthenticationDataSource;
import it.unimib.enjoyn.source.users.BaseAuthenticationDataSource;
import it.unimib.enjoyn.source.users.BaseUserLocalDataSource;
import it.unimib.enjoyn.source.users.BaseUserRemoteDataSource;
import it.unimib.enjoyn.source.users.UserLocalDataSource;
import it.unimib.enjoyn.source.users.UserRemoteDataSource;
import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.WeatherRepository;
import it.unimib.enjoyn.service.WeatherApiService;
import it.unimib.enjoyn.source.BaseWeatherRemoteDataSource;
import it.unimib.enjoyn.source.WeatherRemoteDataSource;
import it.unimib.enjoyn.repository.EventRepository;
import it.unimib.enjoyn.repository.IEventRepository;
import it.unimib.enjoyn.source.events.BaseEventLocalDataSource;
import it.unimib.enjoyn.source.events.BaseEventRemoteDataSource;
import it.unimib.enjoyn.source.events.EventLocalDataSource;
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
    public IUserRepository getUserRepository(Application application){

        BaseUserLocalDataSource userLocalDataSource = new UserLocalDataSource(getLocalDatabase(application));
        BaseUserRemoteDataSource userRemoteDataSource = new UserRemoteDataSource();
        AuthenticationDataSource authenticationDataSource = new AuthenticationDataSource();

        return new UserRepository(userLocalDataSource, userRemoteDataSource, authenticationDataSource);
    }

    public ICategoryRepository getCategoryRepository(){

        CategoryRemoteDataSource categoryRemoteDataSource = new CategoryRemoteDataSource();
        return new CategoryRepository(categoryRemoteDataSource);
    }

    public LocalRoomDatabase getLocalDatabase(Application application) {
        return LocalRoomDatabase.getDatabase(application);
    }

    public IInterestRepository getInterestRepository(Application application) {
        BaseInterestRemoteDataSource interestDataSource = new InterestRemoteDataSource();
        BaseInterestLocalDataSource interestLocalDataSource = new InterestLocalDataSource(getLocalDatabase(application));
        BaseAuthenticationDataSource authenticationDataSource = new AuthenticationDataSource();

        return new InterestRepository(application, interestDataSource, interestLocalDataSource, authenticationDataSource);
    }

    public LocalRoomDatabase getEventDao(Application application) { //istanza di event room database
        return LocalRoomDatabase.getDatabase(application);
    }

    public IWeatherRepository getWeatherRepository(Application application){
        BaseWeatherRemoteDataSource weatherRemoteDataSource;

        weatherRemoteDataSource = new WeatherRemoteDataSource();

        return new WeatherRepository(weatherRemoteDataSource);
    }

    public MapRepository getMapRepository(Application application){
        MapRemoteDataSource mapRemoteDataSource;

        mapRemoteDataSource = new MapRemoteDataSource();

        return new MapRepository(mapRemoteDataSource);
    }

    public IEventRepository getEventRepository(Application application){
        BaseEventLocalDataSource eventLocalDataSource;
        BaseEventRemoteDataSource eventRemoteDataSource;
        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);

        eventRemoteDataSource = new EventRemoteDataSource(jsonParserUtil);
        eventLocalDataSource = new EventLocalDataSource(getEventDao(application));

        return new EventRepository(eventLocalDataSource, eventRemoteDataSource);
    }
}
