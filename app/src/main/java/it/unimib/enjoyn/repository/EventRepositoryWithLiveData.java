package it.unimib.enjoyn.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.source.BaseEventLocalDataSource;
import it.unimib.enjoyn.source.BaseEventRemoteDataSource;
import it.unimib.enjoyn.source.EventCallback;

public class EventRepositoryWithLiveData implements IEventRepositoryWithLiveData, EventCallback {
    private final MutableLiveData<Result> allEventMutableLiveData;
    private final MutableLiveData<Result> favoriteEventMutableLiveData;
    private final MutableLiveData<Result> toDoEventMutableLiveData;
    private final BaseEventLocalDataSource eventLocalDataSource;
    private final BaseEventRemoteDataSource eventRemoteDataSource;

    public EventRepositoryWithLiveData(MutableLiveData<Result> toDoEventMutableLiveData, BaseEventLocalDataSource eventLocalDataSource, BaseEventRemoteDataSource eventRemoteDataSource) {
        allEventMutableLiveData = new MutableLiveData<>();
        favoriteEventMutableLiveData = new MutableLiveData<>();
        this.toDoEventMutableLiveData = toDoEventMutableLiveData;
        this.eventLocalDataSource = eventLocalDataSource;
        this.eventRemoteDataSource = eventRemoteDataSource;
    }

    @Override
    public MutableLiveData<Result> getFavoriteEvent() {
        return null;
    }

    @Override
    public MutableLiveData<Result> getToDoEvent() {
        return null;
    }

    @Override
    public void updateEvent(Event event) {

    }

    @Override
    public void deleteFavoriteEvent() {

    }

    @Override
    public void onSuccessFromLocal(List<Event> eventList) {

    }

    @Override
    public void onFailureFromLocal(Exception exception) {

    }

    @Override
    public void onEventToDoStatusChanged(Event event, List<Event> eventToDo) {

    }

    @Override
    public void onEventToDoStatusChanged(List<Event> event) {

    }

    @Override
    public void onEventFavoriteStatusChanged(List<Event> event) {

    }

    @Override
    public void onDeleteToDoEventSuccess(List<Event> eventToDo) {

    }
}
