package it.unimib.enjoyn.source.events;

import java.util.Map;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;

//TODO da implementare
public abstract class BaseEventRemoteDataSource {
    protected EventCallback eventCallback;

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    public abstract void getEvent(String category);
    public abstract void getEvent();
    public abstract void createEvent(Event event, User user);

    public abstract void updateEvent(String key, Map<String, Object> updateMap);
}
