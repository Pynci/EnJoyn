package it.unimib.enjoyn.repository;

import android.app.Application;

import java.io.IOException;
import java.util.List;

import it.unimib.enjoyn.database.LocalRoomDatabase;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.database.EventsDao;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.util.JSONParserUtil;
import it.unimib.enjoyn.util.ResponseCallback;
import it.unimib.enjoyn.util.ServiceLocator;

public class EventMockRepository implements IEventRepository{

    private final Application application;
    private final ResponseCallback responseCallback;
    private final EventsDao eventsDao;

    public EventMockRepository(Application application, ResponseCallback responseCallback) {
        this.application = application;
        this.responseCallback = responseCallback;
        LocalRoomDatabase localRoomDatabase = ServiceLocator.getInstance().getLocalDatabase(application);
        this.eventsDao = localRoomDatabase.eventDao();
    }

    @Override
    public void fetchEvents(Category category) throws IOException {
        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);

        EventsDatabaseResponse eventsDatabaseResponse = jsonParserUtil.parseJSONEventFileWithGSon("prova.json");;
        saveDataInDatabase(eventsDatabaseResponse.getEvents());
    }

    @Override
    public void getTODOEvents() {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(eventsDao.getTodoEvents(), System.currentTimeMillis());
        });
    }

    @Override
    public void updateEvent(Event event) {
        /**TODO aggiornamento distanza, meteo*/

    }

    public void updateTodo(Event event){
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            eventsDao.updateSingleTodoEvent(event);
            responseCallback.onEventTodoStatusChanged(event);
        });
    }


    @Override
    public void getFavoriteEvents() {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(eventsDao.getFavoriteEvents(), System.currentTimeMillis());
        });
    }
    private void saveDataInDatabase(List<Event> newsList) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Event> allNews = eventsDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (Event event : allNews) {
                // This check works because News and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                if (newsList.contains(event)) {
                    // The primary key and the favorite status is contained only in the News objects
                    // retrieved from the database, and not in the News objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // News object saved in the database, so that it has the primary key and the
                    // favorite status.
                    newsList.set(newsList.indexOf(event), event);
                }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedNewsIds = eventsDao.insertNewsList(newsList);
            for (int i = 0; i < newsList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                newsList.get(i).setId(insertedNewsIds.get(i));
            }

            responseCallback.onSuccess(newsList, System.currentTimeMillis());
        });
    }

}
