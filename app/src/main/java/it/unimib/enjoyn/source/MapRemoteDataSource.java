package it.unimib.enjoyn.source;

import static it.unimib.enjoyn.util.Constants.API_ERROR;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import com.mapbox.geojson.Point;
import com.mapbox.search.QueryType;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.ReverseGeoOptions;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchEngineSettings;
import com.mapbox.search.SearchMultipleSelectionCallback;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.common.IsoCountryCode;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import java.util.ArrayList;
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

    public void startSearchEngine(){
        searchEngine = SearchEngine.createSearchEngineWithBuiltInDataProviders(
                new SearchEngineSettings(MAPBOX_DOWNLOADS_TOKEN)
        );
    }
    public ReverseGeoOptions getReverseOption(Point point) {
        List<QueryType> queryTypeList = new ArrayList<>();
        queryTypeList.add(QueryType.POI);
        queryTypeList.add(QueryType.ADDRESS);
        final ReverseGeoOptions optionsReverse = new ReverseGeoOptions.Builder(point)
                .limit(1)
                .types(queryTypeList)
                .build();

        return  optionsReverse;
    }

    public SearchOptions getSearchOption(Point selfLocation) {
        final SearchOptions options = new SearchOptions.Builder()
                .limit(4)
                .proximity(selfLocation)
                .build();

        return  options;
    }

    public void getMapSuggestion(String searchBarText, Point selfLocation) {
        Log.d("API map", "dentro fetchMapSu su DATASOURCE");
        startSearchEngine();

        searchEngine.search(searchBarText, getSearchOption(selfLocation), searchCallback);

    }

    public void getMapSearch( List<SearchSuggestion> suggestion) {
        Log.d("API map", "dentro fetchMapSu su DATASOURCE");
        startSearchEngine();



        searchEngine.select(suggestion, searchMultipleSelectionCallback);

    }
    public void getMapReverseSearch( Point point) {
        Log.d("API map", "dentro fetchMapSu su DATASOURCE");
        startSearchEngine();
        searchEngine.search( getReverseOption(point), searchReverseCallback);
    }

    SearchMultipleSelectionCallback searchMultipleSelectionCallback = new SearchMultipleSelectionCallback() {
        @Override
        public void onResult(@NonNull List<SearchSuggestion> list, @NonNull List<SearchResult> searchResult, @NonNull ResponseInfo responseInfo) {
            if (searchResult.size() > 0) {
                mapCallBack.onSuccessSearchFromRemote(searchResult);
            } else {
                mapCallBack.onFailureSearchFromRemote(new Exception("Non ci sono posti con questo nome"));
            }
        }
        @Override
        public void onError(@NonNull Exception e) {
            mapCallBack.onFailureSearchFromRemote(new Exception("errore API ricerca multipla"));
        }
    };

    SearchSelectionCallback searchCallback = new SearchSelectionCallback() {


        @Override
        public void onSuggestions(@NonNull List<SearchSuggestion> suggestions, @NonNull ResponseInfo responseInfo) {
            if (suggestions.isEmpty()) {
                mapCallBack.onFailureSuggestionFromRemote(new Exception("nessun suggerimento disponibile"));
                Log.i("SearchApiExample", "No suggestions found");
            } else {
                Log.i("SearchApi", "Search suggestions: " + suggestions + "\nSelecting first...");

                mapCallBack.onSuccessSuggestionFromRemote(suggestions);

            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            //TODO cambiare stringa errore
            mapCallBack.onFailureSuggestionFromRemote(new Exception("errore API suggerimenti"));
        }

        @Override
        public void onResults(@NonNull SearchSuggestion searchSuggestion, @NonNull List<SearchResult> list, @NonNull ResponseInfo responseInfo) {

        }

        @Override
        public void onResult(@NonNull SearchSuggestion searchSuggestion, @NonNull SearchResult searchResult, @NonNull ResponseInfo responseInfo) {
           /* if (searchResult != null) {

                mapCallBack.onSuccessSearchFromRemote(searchResult);
            } else {
*/


        }
    };
    private final SearchCallback searchReverseCallback = new SearchCallback() {
        @SuppressLint("SuspiciousIndentation")
        @Override
        public void onResults(@NonNull List<SearchResult> list, @NonNull ResponseInfo responseInfo) {

            if (list.size() > 0) {
                if(list.size()== 1){
                mapCallBack.onSuccessReverseSearchFromRemote(list.get(0));
                }
            }
            else {
                mapCallBack.onFailureReverseFromRemote(new Exception("non trovo luoghi, riprova "));
            }
        }

        @Override
        public void onError(@NonNull Exception e) {

        }
    };
}








