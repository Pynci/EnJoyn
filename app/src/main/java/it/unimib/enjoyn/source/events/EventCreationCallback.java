package it.unimib.enjoyn.source.events;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;

public interface EventCreationCallback {
    void onRemoteEventCreationAdditionSuccess(Event event, User user);

    void onRemoteEventCreationAdditionFailure(Exception exception);
}
