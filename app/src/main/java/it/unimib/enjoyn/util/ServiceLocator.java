package it.unimib.enjoyn.util;

import android.app.Application;

import it.unimib.enjoyn.database.EventsRoomDatabase;
import it.unimib.enjoyn.repository.EventRepositoryWithLiveData;
import it.unimib.enjoyn.repository.IEventRepositoryWithLiveData;
import it.unimib.enjoyn.source.BaseEventLocalDataSource;
import it.unimib.enjoyn.source.BaseEventRemoteDataSource;
import it.unimib.enjoyn.source.EventLocalDataSource;
import it.unimib.enjoyn.source.EventMockRemoteDataSource;
import it.unimib.enjoyn.source.EventRemoteDataSource;
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

    /**
     * It creates an instance of NewsApiService using Retrofit.
     * @return an instance of NewsApiService.
     */
    /**public NewsApiService getNewsApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.NEWS_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(NewsApiService.class); //creo l'oggetto retrofit, passando il baseUrl,
        //passo il convertitore e ritorno l'istanza creata del client retrofit, passando il nome dell'interfaccia
    }*/

    public EventsRoomDatabase getEventDao(Application application) { //istanza di news room database
        return EventsRoomDatabase.getDatabase(application);
    }

    public IEventRepositoryWithLiveData getEventRepository(Application application){
        BaseEventLocalDataSource eventLocalDataSource;
        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);
        BaseEventRemoteDataSource eventRemoteDataSource = new EventMockRemoteDataSource(jsonParserUtil);
        eventLocalDataSource = new EventLocalDataSource(getEventDao(application));

        return new EventRepositoryWithLiveData(eventLocalDataSource, eventRemoteDataSource);
    }
}
