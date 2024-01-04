package it.unimib.enjoyn.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.IEventRepositoryWithLiveData;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.IMeteoRepository;

public class EventViewModel extends ViewModel {
    private final IEventRepositoryWithLiveData eventRepositoryWithLiveData;
    private MutableLiveData<Result> eventLiveData;
    private MutableLiveData<Result> toDoEventListLiveData;
    private MutableLiveData<Result> favoriteEventListLiveData;
    private final IMeteoRepository weatherRepository;
    private MutableLiveData<Result> weatherListLiveData;

    public EventViewModel(IEventRepositoryWithLiveData eventRepositoryWithLiveData) {
        this.eventRepositoryWithLiveData = eventRepositoryWithLiveData;
        weatherRepository = null;
    }

    public EventViewModel(IMeteoRepository iWeatherRepository) {
        this.weatherRepository = iWeatherRepository;
        eventRepositoryWithLiveData = null;
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
        Log.d("API meteo", "dentro getWeather su viewModel");
        if (weatherListLiveData == null){
            fetchWeather(latitude, logitude);
        }
        return weatherListLiveData;
    }

    private void fetchWeather(String latitude, String longitude){
        Log.d("API meteo", "dentro fetchWeather su viewModel");
        weatherListLiveData = weatherRepository.fetchMeteo(latitude, longitude);
    }
}
