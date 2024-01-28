package it.unimib.enjoyn.source.events;

public abstract class BaseEventCreationRemoteDataSource {
    protected EventCreationCallback eventCreationCallback;

    public void setEventCreationCallback(EventCreationCallback eventCreationCallback) {
        this.eventCreationCallback = eventCreationCallback;
    }
}
