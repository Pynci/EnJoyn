package it.unimib.enjoyn.source.events;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.Callback;

public interface BaseParticipationRemoteDataSource {
    void createParticipation(Event event, User user, Callback callback);

    void isTodo(Event event, String uid, Callback callback);
}
