package it.unimib.enjoyn.source.events;

import java.util.Map;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;

public abstract class BaseEventRemoteDataSource {
    protected EventCallback eventCallback;

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    public abstract void fetchAllEvents();
    public abstract void createEvent(Event event, User user, Callback callback);
    public abstract void updateEvent(String key, Map<String, Object> updateMap);
}
