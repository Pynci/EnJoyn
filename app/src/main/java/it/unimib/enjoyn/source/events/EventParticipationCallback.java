package it.unimib.enjoyn.source.events;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;

public interface EventParticipationCallback {
    void onRemoteEventParticipationAdditionSuccess(Event event, User user);

    void onRemoteEventParticipationAdditionFailure(Exception exception);
}
