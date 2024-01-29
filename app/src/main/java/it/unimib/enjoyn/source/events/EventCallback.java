package it.unimib.enjoyn.source.events;

import java.util.List;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.model.User;

public interface EventCallback {
    void onSuccessFromRemote(EventsDatabaseResponse eventDBResponse, long lastUpdate);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(List<Event> eventList);
    void onFailureFromLocal(Exception exception);
    void onEventToDoStatusChanged(Event event, List<Event> eventToDo);
    void onEventToDoStatusChanged(List<Event> event);
    void onEventFavoriteStatusChanged(List<Event> event);
    void onEventFavoriteStatusChanged(Event event, List<Event> eventFavorite);

    void onRemoteEventAdditionSuccess(Event event, User user);
    void onRemoteEventAdditionFailure(Exception exception);

    void onRemoteEventAdded(Event event);
    void onRemoteEventChanged(Event event);
    void onRemoteEventRemoved(Event event);
    void onRemoteEventFetchFailure(Exception exception);


    //void onDeleteToDoEventSuccess(List<Event> eventToDo);
}
