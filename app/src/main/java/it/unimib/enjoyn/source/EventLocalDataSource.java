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
    public void getEventToDo() {
        EventsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Event> todoEvent = eventDao.getTodoEvents();
            eventCallback.onEventToDoStatusChanged(todoEvent);
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
    public void deleteToDoEvent() {

    }

    @Override
    public void insertEvent(List<Event> eventList) {

    }
}
