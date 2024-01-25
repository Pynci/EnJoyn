package it.unimib.enjoyn.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import java.util.List;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.Weather;
import it.unimib.enjoyn.source.MapRemoteDataSource;
import it.unimib.enjoyn.util.MapCallBack;

public class MapRepository implements MapCallBack {

    private final MutableLiveData<Result> mapMutableLiveData;

    private final MutableLiveData<Result> mapMutableSearchLiveData;

    private final MapRemoteDataSource mapRemoteDataSource;

    public MapRepository( MapRemoteDataSource mapRemoteDataSource) {
        this.mapMutableLiveData = new MutableLiveData<>();
        this.mapMutableSearchLiveData = new MutableLiveData<>();
        this.mapRemoteDataSource = mapRemoteDataSource;
        mapRemoteDataSource.setMapCallBack(this);
    }

    @Override
    public void onSuccessSuggestionFromRemote(List<SearchSuggestion> suggestions) {
        if ( mapMutableLiveData.getValue() != null &&  mapMutableLiveData.getValue().isSuccessful()) {
           // List<SearchSuggestion> suggestions = ((Result.WeatherSuccess) mapMutableLiveData.getValue()).getData().getWeather();

            Result.MapSuggestionSuccess result = new Result.MapSuggestionSuccess(suggestions);
            mapMutableLiveData.postValue(result);
        } else {
            Result.MapSuggestionSuccess result = new Result.MapSuggestionSuccess(suggestions);
            mapMutableLiveData.postValue(result);
        }
    }

    @Override
    public void onSuccessSearchFromRemote(List<SearchResult> searchResult) {
        if (  mapMutableSearchLiveData.getValue() != null &&   mapMutableSearchLiveData.getValue().isSuccessful()) {
            // List<SearchSuggestion> suggestions = ((Result.WeatherSuccess) mapMutableLiveData.getValue()).getData().getWeather();

            Result.MapSearchSuccess result = new Result.MapSearchSuccess(searchResult);
            mapMutableSearchLiveData.postValue(result);
        } else {
            Result.MapSearchSuccess result = new Result.MapSearchSuccess(searchResult);
            mapMutableSearchLiveData.postValue(result);
        }
    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }


    public MutableLiveData<Result> fetchMapSu(String searchBarText) {
        Log.d("API map", "dentro fetchMapSu su Reposity");
        mapRemoteDataSource.getMapSuggestion(searchBarText);

        return mapMutableLiveData;
    }


    public MutableLiveData<Result> fetchMapSearch( List<SearchSuggestion> suggestion) {
        Log.d("API map", "dentro fetchMapSu su Reposity");
        mapRemoteDataSource.getMapSearch(suggestion);

        return mapMutableSearchLiveData;
    }
}
