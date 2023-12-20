package it.unimib.enjoyn.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
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

    public EventRepositoryWithLiveData(BaseEventLocalDataSource eventLocalDataSource, BaseEventRemoteDataSource eventRemoteDataSource) {
        allEventMutableLiveData = new MutableLiveData<>();
        favoriteEventMutableLiveData = new MutableLiveData<>();
        toDoEventMutableLiveData = new MutableLiveData<>();
        this.eventLocalDataSource = eventLocalDataSource;
        this.eventRemoteDataSource = eventRemoteDataSource;
        this.eventLocalDataSource.setEventCallback(this);
        this.eventRemoteDataSource.setEventCallback(this);
    }

    @Override
    public MutableLiveData<Result> fetchNews(String category, int page, long lastUpdate) {
        long currentTime = System.currentTimeMillis();

        // It gets the event from the Web Service if the last download
        // of the news has been performed more than 1000 value ago
        if (/* TODO da aggiungere con dati remoti --> currentTime - lastUpdate > 1000*/ false) {
            eventRemoteDataSource.getEvent(category);
        } else {
            eventLocalDataSource.getEvent();
        }
        return allEventMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getFavoriteEvent() {
        eventLocalDataSource.getFavoriteEvent();
        return favoriteEventMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getToDoEvent() {
        eventLocalDataSource.getToDoEvent();
        return toDoEventMutableLiveData;
    }

    @Override
    public void updateEvent(Event event) {
        eventLocalDataSource.updateEvent(event);
    }


    @Override
    public void onSuccessFromLocal(List<Event> eventList) {
        Result.Success result = new Result.Success(new EventsDatabaseResponse(eventList));
        allEventMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
        Result.Error resultError = new Result.Error(exception.getMessage());
        allEventMutableLiveData.postValue(resultError);
        favoriteEventMutableLiveData.postValue(resultError);
        toDoEventMutableLiveData.postValue(resultError);
    }

    @Override
    public void onEventToDoStatusChanged(Event event, List<Event> eventToDo) {
        Result allEventResult = allEventMutableLiveData.getValue();

        if (allEventResult != null && allEventResult.isSuccess()) {
            List<Event> oldAllEvent = ((Result.Success)allEventResult).getData().getEventList();
            if (oldAllEvent.contains(event)) {
                oldAllEvent.set(oldAllEvent.indexOf(event), event);
                allEventMutableLiveData.postValue(allEventResult);
            }
        }
        toDoEventMutableLiveData.postValue(new Result.Success(new EventsDatabaseResponse(eventToDo)));
    }
    @Override
    public void onEventToDoStatusChanged(List<Event> event) {
        toDoEventMutableLiveData.postValue(new Result.Success(new EventsDatabaseResponse(event)));
    }

    @Override
    public void onEventFavoriteStatusChanged(Event event, List<Event> eventFavorite) {
        Result allEventResult = allEventMutableLiveData.getValue();

        if (allEventResult != null && allEventResult.isSuccess()) {
            List<Event> oldAllEvent = ((Result.Success)allEventResult).getData().getEventList();
            if (oldAllEvent.contains(event)) {
                oldAllEvent.set(oldAllEvent.indexOf(event), event);
                allEventMutableLiveData.postValue(allEventResult);
            }
        }
        favoriteEventMutableLiveData.postValue(new Result.Success(new EventsDatabaseResponse(eventFavorite)));
    }
    @Override
    public void onEventFavoriteStatusChanged(List<Event> event) {
        favoriteEventMutableLiveData.postValue(new Result.Success(new EventsDatabaseResponse(event)));
    }

}
