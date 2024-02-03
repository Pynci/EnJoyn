package it.unimib.enjoyn.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;


public interface IEventRepository {
    MutableLiveData<Result> fetchAllEvents();

//    void updateEvent(String key, Map<String, Object> updateMap);

    MutableLiveData<Result> createEvent(Event event, User user);
}
