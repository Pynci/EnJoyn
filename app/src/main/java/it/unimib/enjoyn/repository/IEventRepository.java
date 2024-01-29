package it.unimib.enjoyn.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;


public interface IEventRepository {
    MutableLiveData<Result> fetchEvent(String category, int page, long lastUpdate);

    MutableLiveData<Result> fetchEvent(long lastUpdate);


    void updateEvent(Event event);

    MutableLiveData<Result> createEvent(Event event, User user);
}
