package it.unimib.enjoyn.source.events;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;

public abstract class BaseEventParticipationRemoteDataSource {
    protected EventParticipationCallback eventParticipationCallback;

    public void setEventParticipationCallback(EventParticipationCallback eventParticipationCallback) {
        this.eventParticipationCallback = eventParticipationCallback;
    }


    public abstract void createEventParticipation(Event event, User user);
}
