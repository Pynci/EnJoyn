package it.unimib.enjoyn.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;


public interface IEventRepositoryWithLiveData {
    MutableLiveData<Result> fetchNews(String category, int page, long lastUpdate);

    MutableLiveData<Result> getFavoriteEvent();

    MutableLiveData<Result> getToDoEvent();

    void updateEvent(Event event);

}
