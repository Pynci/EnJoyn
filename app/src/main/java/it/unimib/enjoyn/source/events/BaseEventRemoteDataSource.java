package it.unimib.enjoyn.source.events;

import java.util.Map;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;

public interface BaseEventRemoteDataSource {

    void fetchAllEvents(String uid, Callback addedCallback, Callback changedCallback, Callback removedCallback, Callback cancelledCallback);

    void createEvent(Event event, User user, Callback callback);

    void updateEvent(String key, Map<String, Object> updateMap, Callback callback);
}
