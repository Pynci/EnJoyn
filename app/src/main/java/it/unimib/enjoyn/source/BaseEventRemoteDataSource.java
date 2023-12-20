package it.unimib.enjoyn.source;

//TODO da implementare
public abstract class BaseEventRemoteDataSource {
    protected EventCallback eventCallback;

    public void setEventCallback(EventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }

    public abstract void getEvent(String country);
}
