package it.unimib.enjoyn.source;

import static it.unimib.enjoyn.util.Costants.UNEXPECTED_ERROR;

import java.util.List;

import it.unimib.enjoyn.database.EventsDao;
import it.unimib.enjoyn.database.EventsRoomDatabase;
import it.unimib.enjoyn.model.Event;

public class EventLocalDataSource  extends BaseEventLocalDataSource{

    private final EventsDao eventDao;

    public EventLocalDataSource(EventsRoomDatabase eventsRoomDatabase) {
        this.eventDao = eventsRoomDatabase.eventDao();
    }

    /**
     * Gets the event from the local database.
     * The method is executed with an ExecutorService defined in EventRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    @Override
    public void getEvent() {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            eventCallback.onSuccessFromLocal(eventDao.getAll());
        });
    }

    @Override
    public void getToDoEvent() {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Event> todoEvent = eventDao.getTodoEvents();
            eventCallback.onEventToDoStatusChanged(todoEvent);
        });
    }

    @Override
    public void getFavoriteEvent() {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            eventCallback.onEventFavoriteStatusChanged(eventDao.getFavoriteEvents());
        });
    }

    @Override
    public void updateEvent(Event event) {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            int rowUpdatedCounter = eventDao.updateSingleTodoEvent(event);

            if (rowUpdatedCounter == 1) {
                Event updatedEvent = eventDao.getEvent(event.getId());
                eventCallback.onEventToDoStatusChanged(updatedEvent, eventDao.getTodoEvents());
            } else {
                eventCallback.onFailureFromLocal(new Exception(UNEXPECTED_ERROR));
            }
        });
    }

    @Override
    public void insertEvent(List<Event> eventList) {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Event> allEvent = eventDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (Event event : allEvent) {
                // This check works because News and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                if (eventList.contains(event)) {
                    // The primary key and the favorite status is contained only in the News objects
                    // retrieved from the database, and not in the News objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // Event object saved in the database, so that it has the primary key and the
                    // favorite status.
                    eventList.set(eventList.indexOf(event), event);
                }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedNewsIds = eventDao.insertEventList(eventList);
            for (int i = 0; i < eventList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                eventList.get(i).setId(insertedNewsIds.get(i));
            }
            //TODO aggiungere categorie (tag) di interesse
            eventCallback.onSuccessFromLocal(eventList);
        });

    }


}
