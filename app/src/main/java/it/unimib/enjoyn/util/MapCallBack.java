package it.unimib.enjoyn.util;

import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import java.util.List;

import it.unimib.enjoyn.model.WeatherApiResponse;

public interface MapCallBack {

    void onSuccessSuggestionFromRemote( List<SearchSuggestion> suggestions);

    void onSuccessSearchFromRemote( SearchResult searchResult);
    void onFailureFromRemote(Exception exception);


}
