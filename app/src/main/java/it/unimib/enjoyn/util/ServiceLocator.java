package it.unimib.enjoyn.util;

import android.app.Application;

import it.unimib.enjoyn.database.EventsRoomDatabase;

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

    /*
    TODO: Qui ci va un sistema per restituire l'istanza per l'accesso al DB di Firebase (? Forse serve o forse no ?)
    Il DB di firebase è un servizio esterno, quindi andrebbe restituito da il Service Locator
    Implementare il metodo corretto.
     */
}
