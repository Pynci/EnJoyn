package it.unimib.enjoyn.util;

import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import java.util.List;

public interface MapCallBack {

    void onSuccessSuggestionFromRemote( List<SearchSuggestion> suggestions);

    void onSuccessSearchFromRemote( List<SearchResult> searchResult);

    void onSuccessReverseSearchFromRemote( SearchResult reverseSearchResult);
    void onFailureFromRemote(Exception exception);


}
