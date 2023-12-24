package it.unimib.enjoyn.util;

import java.util.List;

import it.unimib.enjoyn.model.Event;

public interface ResponseCallback {

    void onSuccess(List<Event> eventList, long lastUpdate);
    void onFailure(String errorMessage);
    void onEventFavoriteStatusChanged(Event event);
    void onEventTodoStatusChanged(Event event);
}
