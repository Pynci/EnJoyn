package it.unimib.enjoyn.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;


public interface IEventRepository {
    MutableLiveData<Result> fetchAllEvents();
    MutableLiveData<Result> joinEvent(Event event, User user);
    MutableLiveData<Result> leaveEvent(Event event, User user);
    MutableLiveData<Result> fetchSingleEvent(Event eventToObserve);
    MutableLiveData<Result> createEvent(Event event, User user);
}
