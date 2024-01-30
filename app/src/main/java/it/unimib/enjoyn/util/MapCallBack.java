package it.unimib.enjoyn.util;

import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import java.util.List;

import it.unimib.enjoyn.model.WeatherApiResponse;

public interface MapCallBack {

    void onSuccessSuggestionFromRemote( List<SearchSuggestion> suggestions);

    void onSuccessSearchFromRemote( List<SearchResult> searchResult);

    void onSuccessReverseSearchFromRemote( SearchResult reverseSearchResult);

    void onFailureReverseFromRemote(Exception e);

    void onFailureSuggestionFromRemote(Exception erroreApiSuggerimenti);

    void onFailureSearchFromRemote(Exception erroreApiRicercaMultipla);
}
