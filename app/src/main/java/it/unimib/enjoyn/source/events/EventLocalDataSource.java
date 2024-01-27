package it.unimib.enjoyn.source.events;

import static it.unimib.enjoyn.util.Constants.UNEXPECTED_ERROR;

import java.util.List;

import it.unimib.enjoyn.database.EventsDao;
import it.unimib.enjoyn.database.LocalRoomDatabase;
import it.unimib.enjoyn.model.Event;

public class EventLocalDataSource  extends BaseEventLocalDataSource{

    private final EventsDao eventDao;

    public EventLocalDataSource(LocalRoomDatabase localRoomDatabase) {
        this.eventDao = localRoomDatabase.eventDao();
    }

    /**
     * Gets the event from the local database.
     * The method is executed with an ExecutorService defined in EventRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    @Override
    public void getEvent() {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            eventCallback.onSuccessFromLocal(eventDao.getAll());
        });
    }



    @Override
    public void updateEvent(Event event) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            int rowUpdatedCounter = eventDao.updateSingleTodoEvent(event);

            if (rowUpdatedCounter == 1) {
                Event updatedEvent = eventDao.getEvent(event.getId());
                //eventCallback.onEventToDoStatusChanged(updatedEvent);
            } else {
                eventCallback.onFailureFromLocal(new Exception(UNEXPECTED_ERROR));
            }
        });
    }

    @Override
    public void insertEvent(List<Event> eventList) {
        LocalRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the event from the database
            List<Event> allEvent = eventDao.getAll();

            // Checks if the event just downloaded has already been downloaded earlier
            // in order to preserve the event status (marked as favorite or not)
            for (Event event : allEvent) {
                // This check works because News and eventSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                if (eventList.contains(event)) {
                    // The primary key and the favorite status is contained only in the event objects
                    // retrieved from the database, and not in the event objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // Event object saved in the database, so that it has the primary key and the
                    // favorite status.
                    eventList.set(eventList.indexOf(event), event);
                }
            }

            // Writes the event in the database and gets the associated primary keys
            List<Long> insertedNewsIds = eventDao.insertEventList(eventList);
            for (int i = 0; i < eventList.size(); i++) {
                // Adds the primary key to the corresponding object event just downloaded so that
                // if the user marks the event as favorite (and vice-versa), we can use its id
                // to know which event in the database must be marked as favorite/not favorite
                eventList.get(i).setId(insertedNewsIds.get(i));
            }
            //TODO aggiungere categorie (tag) di interesse
            eventCallback.onSuccessFromLocal(eventList);
        });

    }


}
