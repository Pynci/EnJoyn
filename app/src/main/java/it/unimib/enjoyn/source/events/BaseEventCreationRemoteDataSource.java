package it.unimib.enjoyn.source.events;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;

public abstract class BaseEventCreationRemoteDataSource {
    protected EventCreationCallback eventCreationCallback;

    public void setEventCreationCallback(EventCreationCallback eventCreationCallback) {
        this.eventCreationCallback = eventCreationCallback;
    }

    public abstract void createEventCreation(Event event, User user);
}
