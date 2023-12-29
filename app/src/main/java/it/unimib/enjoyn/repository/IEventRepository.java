package it.unimib.enjoyn.repository;

import java.io.IOException;

import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Event;

//TODO da eliminare interfaccia
public interface IEventRepository {


    void fetchAllEvents() throws IOException;

    void getTODOEvents();

    void updateEvent(Event event);

    void updateTodo(Event event);

    void getFavoriteEvents();
}
