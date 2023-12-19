package it.unimib.enjoyn.source;

import java.util.List;

import it.unimib.enjoyn.model.Event;

/**
 * Interface to send data from DataSource to Repositories
 * that implement INewsRepositoryWithLiveData interface.
 */
public interface EventCallback {
    //void onSuccessFromRemote(EventApiResponse eventApiResponse, long lastUpdate);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(List<Event> eventList);
    void onFailureFromLocal(Exception exception);
    void onEventToDoStatusChanged(Event event, List<Event> eventToDo);
    void onEventToDoStatusChanged(List<Event> event);
    void onDeleteToDoEventSuccess(List<Event> eventToDo);
}
