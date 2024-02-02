package it.unimib.enjoyn.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.IEventRepository;
import android.util.Log;

import com.mapbox.geojson.Point;
import com.mapbox.search.result.SearchSuggestion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.MapRepository;

public class EventViewModel extends ViewModel {
    private final IEventRepository eventRepository;
    private MutableLiveData<Result> allEvents;
    private MutableLiveData<Result> toDoEventListLiveData;
    private MutableLiveData<Result> favoriteEventListLiveData;
    private final IWeatherRepository weatherRepository;
    private final MapRepository mapRepository;
    private MutableLiveData<Result> weatherListLiveData;

    private MutableLiveData<Result> mapSuggestionListLiveData;
    private MutableLiveData<Result> mapSearchLiveData;

    public EventViewModel(IEventRepository eventRepository, IWeatherRepository iWeatherRepository, MapRepository mapRepository) {
        this.eventRepository = eventRepository;
        this.weatherRepository = iWeatherRepository;
        this.mapRepository = mapRepository;
    }

//    public MutableLiveData<Result> getEvent(String category, long lastUpdate) {
//        if (allEvents == null) {
//            fetchEvent(category, lastUpdate);
//        }
//        return allEvents;
//    }

    public MutableLiveData<Result> getEvent() {
        if (allEvents == null) {
            allEvents = eventRepository.fetchAllEvents();
        }
        return allEvents;
    }

    public void updateEvent(Event event) {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("participants", event.getParticipants());
        eventRepository.updateEvent(event.getEid(), eventMap);
    }

//    //TODO fare metodo con category effettive
//    private void fetchEvent(String category, long lastUpdate) {
//        allEvents = eventRepository.fetchAllEvents();
//    }


    public MutableLiveData<Result> createEvent(Event event, User eventCreator){
        return eventRepository.createEvent(event, eventCreator);
    }


    public MutableLiveData<Result> getWeather(String latitude, String logitude){
        Log.d("API weather", "dentro getWeather su viewModel");
        if (weatherListLiveData == null){
            fetchWeather(latitude, logitude);
        }
        return weatherListLiveData;
    }

    private void fetchWeather(String latitude, String longitude){
        Log.d("API weather", "dentro fetchWeather su viewModel");
        weatherListLiveData = weatherRepository.fetchWeather(latitude, longitude);
    }

    //TODO per quando ricerchi dalla barra
    public MutableLiveData<Result> getMapSuggestion(String searchBarText, Point selfLocation){
        Log.d("API map", "dentro getMap su viewModel");
       // if (mapSuggestionListLiveData == null){
        return  mapRepository.fetchMapSu(searchBarText, selfLocation);
        //fetchMapSuggestion(searchBarText);
        //}
      //  return mapSuggestionListLiveData;
    }

    /*private void fetchMapSuggestion(String searchBarText){
        Log.d("API map", "dentro fetchMapSuggestion su viewModel");
        mapSuggestionListLiveData = mapRepository.fetchMapSu(searchBarText);
    }*/
    public MutableLiveData<Result> getMapSearch( List<SearchSuggestion> suggestion){
        return mapRepository.fetchMapSearch(suggestion);
//        Log.d("API map", "dentro getMap su viewModel");
//        if (mapSearchLiveData == null){
//            fetchMapSearch(suggestion);
//        }
//        return mapSearchLiveData;
    }
    public MutableLiveData<Result> getMapReverseSearch(  Point point){
        return mapRepository.fetchMapReverseSearch(point);
//        Log.d("API map", "dentro getMap su viewModel");
//        if (mapSearchLiveData == null){
//            fetchMapSearch(suggestion);
//        }
//        return mapSearchLiveData;
    }
//
//    private void fetchMapSearch(List<SearchSuggestion> suggestion ){
//        Log.d("API map", "dentro fetchMapSuggestion su viewModel");
//        mapSearchLiveData = mapRepository.fetchMapSearch(suggestion);
//    }

}
