package it.unimib.enjoyn.repository;

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

    private final MapRemoteDataSource mapRemoteDataSource;

    public MapRepository( MapRemoteDataSource mapRemoteDataSource) {
        this.mapMutableLiveData = new MutableLiveData<>();
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
    public void onSuccessSearchFromRemote(SearchResult searchResult) {

    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }


    public MutableLiveData<Result> fetchMapSu(String searchBarText) {
        mapRemoteDataSource.getMapSuggestion(searchBarText);

        return mapMutableLiveData;
    }
}
