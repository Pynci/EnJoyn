package it.unimib.enjoyn.source.events;

import java.util.List;

import it.unimib.enjoyn.model.Event;

public abstract class BaseEventLocalDataSource {

    protected EventCallback eventCallback;

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    public abstract void getEvent();
    public abstract void updateEvent(Event event);
    public abstract void insertEvent(List<Event> eventList);


}
