package it.unimib.enjoyn.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.IEventRepositoryWithLiveData;
import android.util.Log;

import com.mapbox.search.result.SearchSuggestion;

import java.util.List;

import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.MapRepository;

public class EventViewModel extends ViewModel {
    private final IEventRepositoryWithLiveData eventRepositoryWithLiveData;
    private MutableLiveData<Result> eventLiveData;
    private MutableLiveData<Result> toDoEventListLiveData;
    private MutableLiveData<Result> favoriteEventListLiveData;
    private final IWeatherRepository weatherRepository;
    private final MapRepository mapRepository;
    private MutableLiveData<Result> weatherListLiveData;

    private MutableLiveData<Result> mapSuggestionListLiveData;
    private MutableLiveData<Result> mapSearchLiveData;

    public EventViewModel(IEventRepositoryWithLiveData eventRepositoryWithLiveData, IWeatherRepository iWeatherRepository, MapRepository mapRepository) {
        this.eventRepositoryWithLiveData = eventRepositoryWithLiveData;
        this.weatherRepository = iWeatherRepository;
        this.mapRepository = mapRepository;
    }

    /**
     * Returns the LiveData object associated with the
     * event list to the Fragment/Activity.
     *
     * @return The LiveData object associated with the event list.
     */
    public MutableLiveData<Result> getEvent(String category, long lastUpdate) {
        if (eventLiveData == null) {
            fetchEvent(category, lastUpdate);
        }
        return eventLiveData;
    }

    public MutableLiveData<Result> getEvent(long lastUpdate) {
        if (eventLiveData == null) {
            fetchEvent(lastUpdate);
        }
        return eventLiveData;
    }

    /**
     * Returns the LiveData object associated with the
     * list of favorite event to the Fragment/Activity.
     *
     * @return The LiveData object associated with the list of favorite event.
     */
    public MutableLiveData<Result> getFavoriteEventLiveData() {
        if (favoriteEventListLiveData == null) {
            getFavoriteEvent();
        }
        return favoriteEventListLiveData;
    }

    public MutableLiveData<Result> getToDoEventLiveData() {
        if (toDoEventListLiveData == null) {
            getToDoEvent();
        }
        return toDoEventListLiveData;
    }

    /**
     * Updates the event status.
     *
     * @param event The event to be updated.
     */
    public void updateEvent(Event event) {
        eventRepositoryWithLiveData.updateEvent(event);
    }

    /**
     * It uses the Repository to download the event list
     * and to associate it with the LiveData object.
     */
    private void fetchEvent(long lastUpdate) {
        eventLiveData = eventRepositoryWithLiveData.fetchEvent(lastUpdate);
    }

    //TODO fare metodo con category effettive
    private void fetchEvent(String category, long lastUpdate) {
        eventLiveData = eventRepositoryWithLiveData.fetchEvent(lastUpdate);
    }

    /**
     * It uses the Repository to get the list of favorite event
     * and to associate it with the LiveData object.
     */
    private void getFavoriteEvent() {
        favoriteEventListLiveData = eventRepositoryWithLiveData.getFavoriteEvent();
    }

    private void getToDoEvent() {
        toDoEventListLiveData = eventRepositoryWithLiveData.getToDoEvent();
    }

    /**
     * Removes the event from the list of favorite event.
     *
     * @param event The event to be removed from the list of favorite event.
     */
    public void removeFromFavorite(Event event) {
        eventRepositoryWithLiveData.updateEvent(event);
    }

    public void removeFromToDo(Event event) {
        eventRepositoryWithLiveData.updateEvent(event);
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
    public MutableLiveData<Result> getMapSuggestion(String searchBarText){
        Log.d("API map", "dentro getMap su viewModel");
       // if (mapSuggestionListLiveData == null){
           return  mapRepository.fetchMapSu(searchBarText);
        //fetchMapSuggestion(searchBarText);
        //}
      //  return mapSuggestionListLiveData;
    }

    private void fetchMapSuggestion(String searchBarText){
        Log.d("API map", "dentro fetchMapSuggestion su viewModel");
        mapSuggestionListLiveData = mapRepository.fetchMapSu(searchBarText);
    }
    public MutableLiveData<Result> getMapSearch( List<SearchSuggestion> suggestion){
        Log.d("API map", "dentro getMap su viewModel");
        if (mapSearchLiveData == null){
            fetchMapSearch(suggestion);
        }
        return mapSearchLiveData;
    }

    private void fetchMapSearch(List<SearchSuggestion> suggestion ){
        Log.d("API map", "dentro fetchMapSuggestion su viewModel");
        mapSearchLiveData = mapRepository.fetchMapSearch(suggestion);
    }

}
