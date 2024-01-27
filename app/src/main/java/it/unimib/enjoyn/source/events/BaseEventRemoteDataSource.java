package it.unimib.enjoyn.source.events;

import it.unimib.enjoyn.model.Event;

//TODO da implementare
public abstract class BaseEventRemoteDataSource {
    protected EventCallback eventCallback;

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    public abstract void getEvent(String category);
    public abstract void getEvent();
    public abstract void createEvent(Event event);
}
