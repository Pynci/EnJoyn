package it.unimib.enjoyn.source.events;

import java.util.List;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.model.User;

public interface EventCallback {
    void onSuccessFromLocal(List<Event> eventList);
    void onFailureFromLocal(Exception exception);
}
