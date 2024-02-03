package it.unimib.enjoyn.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.events.BaseEventLocalDataSource;
import it.unimib.enjoyn.source.events.BaseParticipationRemoteDataSource;
import it.unimib.enjoyn.source.events.BaseEventRemoteDataSource;
import it.unimib.enjoyn.source.users.BaseAuthenticationDataSource;

public class EventRepository implements IEventRepository {
    private final MutableLiveData<Result> allEventsMutableLiveData;
    private final MutableLiveData<Result> favoriteEventMutableLiveData;
    private final MutableLiveData<Result> toDoEventsMutableLiveData;
    private final MutableLiveData<Result> eventCreation;
    private final MutableLiveData<Result> eventParticipation;
    private final List<Event> eventsList;

    private final BaseEventLocalDataSource eventLocalDataSource;
    private final BaseEventRemoteDataSource eventRemoteDataSource;
    private final BaseParticipationRemoteDataSource eventParticipationRemoteDataSource;
    private final BaseAuthenticationDataSource authenticationDataSource;

    public EventRepository(BaseEventLocalDataSource eventLocalDataSource,
                           BaseEventRemoteDataSource eventRemoteDataSource,
                           BaseParticipationRemoteDataSource eventParticipationRemoteDataSource,
                           BaseAuthenticationDataSource authenticationDataSource) {
        allEventsMutableLiveData = new MutableLiveData<>();
        favoriteEventMutableLiveData = new MutableLiveData<>();
        toDoEventsMutableLiveData = new MutableLiveData<>();
        this.eventLocalDataSource = eventLocalDataSource;
        this.eventRemoteDataSource = eventRemoteDataSource;
        this.authenticationDataSource = authenticationDataSource;
        this.eventParticipationRemoteDataSource = eventParticipationRemoteDataSource;
        eventCreation = new MutableLiveData<>();
        eventsList = new ArrayList<>();
        eventParticipation = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> fetchAllEvents() {
        eventRemoteDataSource.fetchAllEvents(authenticationDataSource.getCurrentUserUID(),
                result -> {
                    Event event = ((Result.SingleEventSuccess) result).getEvent();
                    addEvent(event, allEventsMutableLiveData);
                },
                result -> {
                    Event event = ((Result.SingleEventSuccess) result).getEvent();
                    Event oldEvent = findOldEvent(event);
                    replaceEvent(event, oldEvent, allEventsMutableLiveData);
                },
                result -> {
                    Event event = ((Result.SingleEventSuccess) result).getEvent();
                    removeEvent(event, allEventsMutableLiveData);
                },
                allEventsMutableLiveData::postValue);
        return allEventsMutableLiveData;
    }

    public MutableLiveData<Result> fetchTodoEvents(){
        eventRemoteDataSource.fetchAllEvents(authenticationDataSource.getCurrentUserUID(),
                result -> {
                    Event event = ((Result.SingleEventSuccess) result).getEvent();
                    eventParticipationRemoteDataSource.isTodo(event, authenticationDataSource.getCurrentUserUID(),
                        resultTodo -> {
                            if(resultTodo.isSuccessful() && ((Result.BooleanSuccess) resultTodo).getData()){
                                addEvent(event, toDoEventsMutableLiveData);
                            }
                        });
                },
                result -> {
                    Event event = ((Result.SingleEventSuccess) result).getEvent();
                    Event oldEvent = findOldEvent(event);
                    eventParticipationRemoteDataSource.isTodo(event, authenticationDataSource.getCurrentUserUID(),
                            resultTodo -> {
                                if(resultTodo.isSuccessful() && ((Result.BooleanSuccess) resultTodo).getData()){
                                    replaceEvent(event, oldEvent, toDoEventsMutableLiveData);
                                }
                            });
                },
                result -> {
                    Event event = ((Result.SingleEventSuccess) result).getEvent();
                    eventParticipationRemoteDataSource.isTodo(event, authenticationDataSource.getCurrentUserUID(),
                            resultTodo -> {
                                if(resultTodo.isSuccessful() && ((Result.BooleanSuccess) resultTodo).getData()){
                                    removeEvent(event, toDoEventsMutableLiveData);
                                }
                            });
                },
                toDoEventsMutableLiveData::postValue);
        return toDoEventsMutableLiveData;
    }

    private void removeEvent(Event event, MutableLiveData<Result> mutableLiveData) {
        eventsList.remove(event);
        mutableLiveData.postValue(
                new Result.EventSuccess(new EventsDatabaseResponse(eventsList)));
    }

    private void replaceEvent(Event event, Event oldEvent, MutableLiveData<Result> mutableLiveData) {
        if(oldEvent != null){
            replaceEvent(oldEvent, event);
            mutableLiveData.postValue(
                    new Result.EventSuccess(new EventsDatabaseResponse(eventsList)));
        }
    }

    private void addEvent(Event event, MutableLiveData<Result> mutableLiveData) {
        eventsList.add(event);
        mutableLiveData.postValue(
                new Result.EventSuccess(new EventsDatabaseResponse(eventsList)));
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
        eventParticipationRemoteDataSource.createParticipation(event, user, result -> {
            if(result.isSuccessful()){
                newParticipant(event);
            }
            else{
                eventParticipation.postValue(result);
            }
        });
        return eventParticipation;
    }

    private void newParticipant(Event event){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("participants", event.getParticipants()+1);
        updateEvent(event.getEid(), updateMap);
    }

    public MutableLiveData<Result> fetchEventParticipations(Event event){

        return null;
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
