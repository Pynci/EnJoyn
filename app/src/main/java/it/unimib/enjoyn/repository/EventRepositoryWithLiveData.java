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
    public MutableLiveData<Result> fetchEvent(String category, int page, long lastUpdate) {
        return null;
    }

    @Override
    public MutableLiveData<Result> fetchEvent(long lastUpdate) {
        long currentTime = System.currentTimeMillis();
        boolean  getDB = true;
        // It gets the event from the Web Service if the last download
        // of the news has been performed more than 1000 value ago
        if (getDB) {
            eventRemoteDataSource.getEvent();
            getDB = false;
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
    public void onSuccessFromRemote(EventsDatabaseResponse eventDBResponse, long lastUpdate) {
        eventLocalDataSource.insertEvent(eventDBResponse.getEventList());
    }

    @Override
    public void onFailureFromRemote(Exception exception) {
        Result.EventError result = new Result.EventError(exception.getMessage());
        allEventMutableLiveData.postValue(result);
    }
//TODO da fixare perché risulta  eventList.size = 0
    @Override
    public void onSuccessFromLocal(List<Event> eventList) {
        Result.EventSuccess result = new Result.EventSuccess(new EventsDatabaseResponse(eventList));
        allEventMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
        Result.EventError resultError = new Result.EventError(exception.getMessage());
        allEventMutableLiveData.postValue(resultError);
        favoriteEventMutableLiveData.postValue(resultError);
        toDoEventMutableLiveData.postValue(resultError);
    }

    @Override
    public void onEventToDoStatusChanged(Event event, List<Event> eventToDo) {
        Result allEventResult = allEventMutableLiveData.getValue();

        if (allEventResult != null && allEventResult.isSuccess()) {
            List<Event> oldAllEvent = ((Result.EventSuccess)allEventResult).getData().getEventList();
            if (oldAllEvent.contains(event)) {
                oldAllEvent.set(oldAllEvent.indexOf(event), event);
                allEventMutableLiveData.postValue(allEventResult);
            }
        }
        toDoEventMutableLiveData.postValue(new Result.EventSuccess(new EventsDatabaseResponse(eventToDo)));

    }
    @Override
    public void onEventToDoStatusChanged(List<Event> event) {
        toDoEventMutableLiveData.postValue(new Result.EventSuccess(new EventsDatabaseResponse(event)));
    }

    @Override
    public void onEventFavoriteStatusChanged(Event event, List<Event> eventFavorite) {
        Result allEventResult = allEventMutableLiveData.getValue();

        if (allEventResult != null && allEventResult.isSuccess()) {
            List<Event> oldAllEvent = ((Result.EventSuccess)allEventResult).getData().getEventList();
            if (oldAllEvent.contains(event)) {
                oldAllEvent.set(oldAllEvent.indexOf(event), event);
                allEventMutableLiveData.postValue(allEventResult);
            }
        }
        toDoEventMutableLiveData.postValue(new Result.EventSuccess(new EventsDatabaseResponse(eventFavorite)));
    }
    @Override
    public void onEventFavoriteStatusChanged(List<Event> event) {
        favoriteEventMutableLiveData.postValue(new Result.EventSuccess(new EventsDatabaseResponse(event)));
    }

}
