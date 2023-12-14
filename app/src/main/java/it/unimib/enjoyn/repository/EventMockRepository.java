package it.unimib.enjoyn.repository;

import android.app.Application;

import java.util.List;

import it.unimib.enjoyn.Category;
import it.unimib.enjoyn.Event;
import it.unimib.enjoyn.database.EventsDao;
import it.unimib.enjoyn.database.EventsRoomDatabase;
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

    @Override
    public void fetchEvents(Category category) {

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


}
