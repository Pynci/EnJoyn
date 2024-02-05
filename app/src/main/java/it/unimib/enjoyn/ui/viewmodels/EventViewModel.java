package it.unimib.enjoyn.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.IEventRepository;

import com.mapbox.geojson.Point;
import com.mapbox.search.result.SearchSuggestion;

import java.util.List;

import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.MapRepository;

public class EventViewModel extends ViewModel {
    private final IEventRepository eventRepository;
    private MutableLiveData<Result> allEvents;
    private final IWeatherRepository weatherRepository;
    private final MapRepository mapRepository;
    private MutableLiveData<Result> weatherListLiveData;

    public EventViewModel(IEventRepository eventRepository, IWeatherRepository iWeatherRepository, MapRepository mapRepository) {
        this.eventRepository = eventRepository;
        this.weatherRepository = iWeatherRepository;
        this.mapRepository = mapRepository;
    }

    public MutableLiveData<Result> getAllEvents() {
        if (allEvents == null) {
            allEvents = eventRepository.fetchAllEvents();
        }
        return allEvents;
    }

    public MutableLiveData<Result> refreshEvent(Event event){
        return eventRepository.fetchSingleEvent(event);
    }

    public MutableLiveData<Result> joinEvent(Event event, User user){
        return eventRepository.joinEvent(event, user);
    }

    public MutableLiveData<Result> leaveEvent(Event event, User user){
        return eventRepository.leaveEvent(event, user);
    }


    public MutableLiveData<Result> createEvent(Event event, User eventCreator){
        return eventRepository.createEvent(event, eventCreator);
    }


    public MutableLiveData<Result> getWeather(String latitude, String logitude){
        if (weatherListLiveData == null){
            fetchWeather(latitude, logitude);
        }
        return weatherListLiveData;
    }

    private void fetchWeather(String latitude, String longitude){
        weatherListLiveData = weatherRepository.fetchWeather(latitude, longitude);
    }

    public MutableLiveData<Result> getMapSuggestion(String searchBarText, Point selfLocation){
        return  mapRepository.fetchMapSu(searchBarText, selfLocation);
    }

    public MutableLiveData<Result> getMapSearch( List<SearchSuggestion> suggestion){
        return mapRepository.fetchMapSearch(suggestion);
    }
    public MutableLiveData<Result> getMapReverseSearch(  Point point){
        return mapRepository.fetchMapReverseSearch(point);
    }

}
