package it.unimib.enjoyn.util;

import android.app.Application;

import it.unimib.enjoyn.database.LocalRoomDatabase;
import it.unimib.enjoyn.repository.category.CategoryRepository;
import it.unimib.enjoyn.repository.category.ICategoryRepository;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.repository.user.UserRepository;
import it.unimib.enjoyn.source.category.CategoryRemoteDataSource;
import it.unimib.enjoyn.source.interests.BaseInterestLocalDataSource;
import it.unimib.enjoyn.source.interests.BaseInterestRemoteDataSource;
import it.unimib.enjoyn.source.interests.InterestLocalDataSource;
import it.unimib.enjoyn.source.interests.InterestRemoteDataSource;
import it.unimib.enjoyn.source.user.AuthenticationDataSource;
import it.unimib.enjoyn.source.user.BaseUserLocalDataSource;
import it.unimib.enjoyn.source.user.BaseUserRemoteDataSource;
import it.unimib.enjoyn.source.user.UserLocalDataSource;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;
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
    public IUserRepository getUserRepository(Application application){

        BaseUserLocalDataSource userLocalDataSource = new UserLocalDataSource(getLocalDatabase(application));
        BaseUserRemoteDataSource userRemoteDataSource = new UserRemoteDataSource();
        BaseInterestRemoteDataSource interestDataSource = new InterestRemoteDataSource();
        BaseInterestLocalDataSource interestLocalDataSource = new InterestLocalDataSource(getLocalDatabase(application));
        AuthenticationDataSource authenticationDataSource = new AuthenticationDataSource();

        return new UserRepository(userLocalDataSource, userRemoteDataSource,
                authenticationDataSource, interestDataSource, interestLocalDataSource);
    }

    public ICategoryRepository getCategoryRepository(){

        CategoryRemoteDataSource categoryRemoteDataSource = new CategoryRemoteDataSource();
        return new CategoryRepository(categoryRemoteDataSource);
    }

    public LocalRoomDatabase getLocalDatabase(Application application) {
        return LocalRoomDatabase.getDatabase(application);
    }

    public LocalRoomDatabase getEventDao(Application application) { //istanza di event room database
        return LocalRoomDatabase.getDatabase(application);
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
