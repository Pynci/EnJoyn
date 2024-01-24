package it.unimib.enjoyn.source;

import static it.unimib.enjoyn.util.Constants.API_ERROR;

import android.util.Log;

import androidx.annotation.NonNull;

import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchEngineSettings;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.util.MapCallBack;

public class MapRemoteDataSource {
    private static final String MAPBOX_DOWNLOADS_TOKEN = "sk.eyJ1IjoicmVhbHB5bmNpIiwiYSI6ImNscnFtaGhhOTA1NWQybG56aXRqNHptM2wifQ.hw772niii39PmdUPCFB99A";
    protected MapCallBack mapCallBack;
    private SearchEngine searchEngine;

    public void setMapCallBack(MapCallBack mapCallBack) {
        this.mapCallBack = mapCallBack;
    }
    final SearchOptions options = new SearchOptions.Builder()
            .limit(4)
            .build();
    public void getMapSuggestion(String searchBarText) {
        Log.d("API map", "dentro fetchMapSu su DATASOURCE");
        searchEngine = SearchEngine.createSearchEngineWithBuiltInDataProviders(
                new SearchEngineSettings(MAPBOX_DOWNLOADS_TOKEN)
        );

        searchEngine.search(searchBarText, options, searchCallback);

    }

    SearchSelectionCallback searchCallback = new SearchSelectionCallback() {


        @Override
        public void onSuggestions(@NonNull List<SearchSuggestion> suggestions, @NonNull ResponseInfo responseInfo) {
            if (suggestions.isEmpty()) {
                Log.i("SearchApiExample", "No suggestions found");
            } else {
                Log.i("SearchApi", "Search suggestions: " + suggestions + "\nSelecting first...");
                mapCallBack.onSuccessSuggestionFromRemote(suggestions);
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            //TODO cambiare stringa errore
            mapCallBack.onFailureFromRemote(new Exception(API_ERROR));
        }

        @Override
        public void onResults(@NonNull SearchSuggestion searchSuggestion, @NonNull List<SearchResult> list, @NonNull ResponseInfo responseInfo) {
            //non serve
        }

        @Override
        public void onResult(@NonNull SearchSuggestion searchSuggestion, @NonNull SearchResult searchResult, @NonNull ResponseInfo responseInfo) {
            if (searchResult != null) {
                mapCallBack.onSuccessSearchFromRemote(searchResult);
            } else {


            }
        }
    };
}








