package it.unimib.enjoyn.repository;

import it.unimib.enjoyn.Category;
import it.unimib.enjoyn.Event;

public interface IEventRepository {


    void fetchEvents(Category category);

    void getTODOEvents();

    void updateEvent(Event event);

    void updateTodo(Event event);

    void getFavoriteEvents();
}
