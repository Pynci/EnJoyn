package it.unimib.enjoyn.source.events;

public abstract class BaseEventParticipationRemoteDataSource {
    protected EventParticipationCallback eventParticipationCallback;

    public void setEventParticipationCallback(EventParticipationCallback eventParticipationCallback) {
        this.eventParticipationCallback = eventParticipationCallback;
    }


}
