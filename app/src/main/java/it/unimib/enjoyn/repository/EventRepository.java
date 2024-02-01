package it.unimib.enjoyn.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.events.BaseEventLocalDataSource;
import it.unimib.enjoyn.source.events.BaseParticipationRemoteDataSource;
import it.unimib.enjoyn.source.events.BaseEventRemoteDataSource;
import it.unimib.enjoyn.source.events.EventCallback;

public class EventRepository implements IEventRepository, EventCallback {
    private final MutableLiveData<Result> allEventsMutableLiveData;
    private final MutableLiveData<Result> favoriteEventMutableLiveData;
    private final MutableLiveData<Result> toDoEventMutableLiveData;
    private final MutableLiveData<Result> eventCreation;
    private final MutableLiveData<Result> eventParticipation;
    private final List<Event> eventsList;

    private final BaseEventLocalDataSource eventLocalDataSource;
    private final BaseEventRemoteDataSource eventRemoteDataSource;
    private final BaseParticipationRemoteDataSource eventParticipationRemoteDataSource;

    public EventRepository(BaseEventLocalDataSource eventLocalDataSource,
                           BaseEventRemoteDataSource eventRemoteDataSource,
                           BaseParticipationRemoteDataSource eventParticipationRemoteDataSource) {
        allEventsMutableLiveData = new MutableLiveData<>();
        favoriteEventMutableLiveData = new MutableLiveData<>();
        toDoEventMutableLiveData = new MutableLiveData<>();
        this.eventLocalDataSource = eventLocalDataSource;
        this.eventRemoteDataSource = eventRemoteDataSource;
        this.eventLocalDataSource.setEventCallback(this);
        this.eventRemoteDataSource.setEventCallback(this);
        this.eventParticipationRemoteDataSource = eventParticipationRemoteDataSource;
        eventCreation = new MutableLiveData<>();
        eventsList = new ArrayList<>();
        eventParticipation = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> fetchAllEvents() {
        eventRemoteDataSource.fetchAllEvents();
        return allEventsMutableLiveData;
    }

    @Override
    public void updateEvent(String key, Map<String, Object> updateMap) {
        eventRemoteDataSource.updateEvent(key, updateMap);
    }

    @Override
    public MutableLiveData<Result> createEvent(Event event, User user){
        eventRemoteDataSource.createEvent(event, user, result -> {
            if(result.isSuccessful()){
                eventParticipationRemoteDataSource.createParticipation(event, user, eventCreation::postValue);
            }
            else{
                eventCreation.postValue(result);
            }
        });
        return eventCreation;
    }

    public MutableLiveData<Result> joinEvent(Event event, User user){
        eventParticipationRemoteDataSource.createParticipation(event, user, eventParticipation::postValue);
        return eventParticipation;
    }

    public MutableLiveData<Result> fetchEventParticipations(Event event){

        return null;
    }


    // callbacks

    @Override
    public void onSuccessFromLocal(List<Event> eventList) {
        Result.EventSuccess result = new Result.EventSuccess(new EventsDatabaseResponse(eventList));
        allEventsMutableLiveData.postValue(result);
    }

    @Override
    public void onFailureFromLocal(Exception exception) {
        Result.EventError resultError = new Result.EventError(exception.getMessage());
        allEventsMutableLiveData.postValue(resultError);
        favoriteEventMutableLiveData.postValue(resultError);
        toDoEventMutableLiveData.postValue(resultError);
    }

    @Override
    public void onRemoteEventAdded(Event event) {
        eventsList.add(event);
        allEventsMutableLiveData.postValue(
                new Result.EventSuccess(new EventsDatabaseResponse(eventsList)));
    }

    @Override
    public void onRemoteEventChanged(Event event) {
        Event oldEvent = findOldEvent(event);
        if(oldEvent != null){
            replaceEvent(oldEvent, event);
            allEventsMutableLiveData.postValue(
                    new Result.EventSuccess(new EventsDatabaseResponse(eventsList)));
        }
    }

    @Override
    public void onRemoteEventRemoved(Event event) {
        eventsList.remove(event);
        allEventsMutableLiveData.postValue(
                new Result.EventSuccess(new EventsDatabaseResponse(eventsList)));
    }

    @Override
    public void onRemoteEventFetchFailure(Exception exception) {
        allEventsMutableLiveData.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    private Event findOldEvent(Event newEvent){
        for(Event event : eventsList){
            if(event.getEid().equals(newEvent.getEid())){
                return event;
            }
        }
        return null;
    }
    private void replaceEvent(Event oldEvent, Event newEvent){
        int index = eventsList.indexOf(oldEvent);
        eventsList.set(index, newEvent);
    }
}
