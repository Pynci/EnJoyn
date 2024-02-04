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
import it.unimib.enjoyn.source.events.BaseParticipationRemoteDataSource;
import it.unimib.enjoyn.source.events.BaseEventRemoteDataSource;
import it.unimib.enjoyn.source.users.BaseAuthenticationDataSource;

public class EventRepository implements IEventRepository {
    private final MutableLiveData<Result> allEvents;
    private MutableLiveData<Result> singleEvent;
    private Event currentlyObservedEvent;
    private final MutableLiveData<Result> eventCreation;
    private final MutableLiveData<Result> eventLeaveParticipation;
    private final MutableLiveData<Result> eventJoinParticipation;
    private final List<Event> eventsList;

    private final BaseEventRemoteDataSource eventRemoteDataSource;
    private final BaseParticipationRemoteDataSource participationRemoteDataSource;
    private final BaseAuthenticationDataSource authenticationDataSource;

    public EventRepository(BaseEventRemoteDataSource eventRemoteDataSource,
                           BaseParticipationRemoteDataSource participationRemoteDataSource,
                           BaseAuthenticationDataSource authenticationDataSource) {
        allEvents = new MutableLiveData<>();
        this.eventRemoteDataSource = eventRemoteDataSource;
        this.authenticationDataSource = authenticationDataSource;
        this.participationRemoteDataSource = participationRemoteDataSource;
        eventCreation = new MutableLiveData<>();
        eventsList = new ArrayList<>();
        eventLeaveParticipation = new MutableLiveData<>();
        eventJoinParticipation = new MutableLiveData<>();
        singleEvent = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> fetchAllEvents() {
        eventRemoteDataSource.fetchAllEvents(
                result -> {
                    Event event = ((Result.SingleEventSuccess) result).getEvent();
                    participationRemoteDataSource.isTodo(event, authenticationDataSource.getCurrentUserUID(),
                            resultTodo -> {
                                if(resultTodo.isSuccessful()){
                                    event.setTodo(((Result.BooleanSuccess) resultTodo).getData());
                                    addEvent(event, allEvents);
                                }
                            });
                },
                result -> {
                    Event event = ((Result.SingleEventSuccess) result).getEvent();
                    Event oldEvent = findOldEvent(event);
                    participationRemoteDataSource.isTodo(event, authenticationDataSource.getCurrentUserUID(),
                            resultTodo -> {
                                if(resultTodo.isSuccessful()){
                                    event.setTodo(((Result.BooleanSuccess) resultTodo).getData());
                                    replaceEvent(event, oldEvent, allEvents);
                                }
                            });
                },
                result -> {
                    Event event = ((Result.SingleEventSuccess) result).getEvent();
                    participationRemoteDataSource.isTodo(event, authenticationDataSource.getCurrentUserUID(),
                            resultTodo -> {
                                if(resultTodo.isSuccessful()){
                                    event.setTodo(((Result.BooleanSuccess) resultTodo).getData());
                                    removeEvent(event, allEvents);
                                }
                            });
                },
                allEvents::postValue);
        return allEvents;
    }

    /*
    Piazza nel livedata ritornato un SingleEventSuccess contenente l'evento con
    i dati aggiornati ad ogni cambio nel nodo firebase.
    Se l'evento viene rimosso o non esiste, nel livedata ci piazza un SingleEventSuccess
    contenente null.
     */
    @Override
    public MutableLiveData<Result> fetchSingleEvent(Event eventToObserve){
        if(currentlyObservedEvent != null){
            singleEvent = new MutableLiveData<>();
        }
        currentlyObservedEvent = eventToObserve;
        eventRemoteDataSource.fetchSingleEvent(eventToObserve,
                result -> {
                    Event event = ((Result.SingleEventSuccess) result).getEvent();
                    if(event != null && event.equals(currentlyObservedEvent)){
                        participationRemoteDataSource.isTodo(event, authenticationDataSource.getCurrentUserUID(),
                                resultTodo -> {
                                    if(resultTodo.isSuccessful()){
                                        event.setTodo(((Result.BooleanSuccess) resultTodo).getData());
                                        singleEvent.postValue(new Result.SingleEventSuccess(event));
                                    }
                                });
                    }
                    else{
                        singleEvent.postValue(new Result.SingleEventSuccess(null));
                    }
                },
                singleEvent::postValue
        );
        return singleEvent;
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
    public MutableLiveData<Result> createEvent(Event event, User user){
        eventRemoteDataSource.createEvent(event, user, result -> {
            if(result.isSuccessful()){
                participationRemoteDataSource.createParticipation(event, user, eventCreation::postValue);
            }
            else{
                eventCreation.postValue(result);
            }
        });
        return eventCreation;
    }

    public MutableLiveData<Result> joinEvent(Event event, User user){
        participationRemoteDataSource.createParticipation(event, user, result -> {
            if(result.isSuccessful()){
                addParticipant(event);
            }
            else{
                eventJoinParticipation.postValue(result);
            }
        });
        return eventJoinParticipation;
    }

    private void addParticipant(Event event){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("participants", event.getParticipants()+1);
        eventRemoteDataSource.updateEvent(event.getEid(), updateMap, eventJoinParticipation::postValue);
    }

    public MutableLiveData<Result> leaveEvent(Event event, User user){
        participationRemoteDataSource.deleteParticipation(event, user, result -> {
            if(result.isSuccessful()){
                removeParticipant(event);
            }
            else{
                eventLeaveParticipation.postValue(result);
            }
        });
        return eventLeaveParticipation;
    }

    private void removeParticipant(Event event){
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("participants", event.getParticipants()-1);
        eventRemoteDataSource.updateEvent(event.getEid(), updateMap, eventLeaveParticipation::postValue);
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
