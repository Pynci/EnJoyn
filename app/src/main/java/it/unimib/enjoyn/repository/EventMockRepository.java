package it.unimib.enjoyn.repository;

import android.app.Application;

import java.io.IOException;
import java.util.List;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.database.EventsDao;
import it.unimib.enjoyn.database.EventsRoomDatabase;
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
        EventsRoomDatabase eventsRoomDatabase = ServiceLocator.getInstance().getEventDao(application);
        this.eventsDao = eventsRoomDatabase.eventDao();
    }

    public void fetchAllEvents() throws IOException {

        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);
        EventsDatabaseResponse eventsDatabaseResponse =null;
         eventsDatabaseResponse = jsonParserUtil.parseJSONEventFileWithGSon("prova.json");
        saveDataInDatabase(eventsDatabaseResponse.getEventList());


    }



    @Override
    public void getTODOEvents() {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(eventsDao.getTodoEvents(), System.currentTimeMillis());
        });
    }

    @Override
    public void updateEvent(Event event) {
        /**TODO aggiornamento distanza, meteo*/
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            eventsDao.updateSingleTodoEvent(event);
            responseCallback.onEventTodoStatusChanged(event);
        });
    }

    public void updateTodo(Event event){
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            eventsDao.updateSingleTodoEvent(event);
            responseCallback.onEventTodoStatusChanged(event);
        });
    }


    @Override
    public void getFavoriteEvents() {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            responseCallback.onSuccess(eventsDao.getFavoriteEvents(), System.currentTimeMillis());
        });
    }
    private void saveDataInDatabase(List<Event> eventList) {

        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            boolean contains = false;
            List<Event> allEvent = eventsDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (Event event : allEvent) {
                // This check works because News and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                contains = eventList.contains(event);
               if (eventList.contains(event)) {
                    // The primary key and the favorite status is contained only in the News objects
                    // retrieved from the database, and not in the News objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // News object saved in the database, so that it has the primary key and the
                    // favorite status.
                    eventList.set(eventList.indexOf(event), event);
               }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedNewsIds = eventsDao.insertEventList(eventList);
            for (int i = 0; i < eventList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                eventList.get(i).setId(insertedNewsIds.get(i));
            }

            responseCallback.onSuccess(eventList, System.currentTimeMillis());
        });
    }

}
