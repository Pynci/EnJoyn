package it.unimib.enjoyn.util;

import java.util.List;

import it.unimib.enjoyn.Event;

public interface ResponseCallback {

    void onSuccess(List<Event> newsList, long lastUpdate);
    void onFailure(String errorMessage);
    void onEventFavoriteStatusChanged(Event event);
    void onEventTodoStatusChanged(Event event);
}
