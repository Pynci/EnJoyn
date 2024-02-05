package it.unimib.enjoyn.source;



import static it.unimib.enjoyn.util.Constants.API_MULTIPLE_SEARCH_ERROR;
import static it.unimib.enjoyn.util.Constants.NO_PLACES_AVAILABLE;
import static it.unimib.enjoyn.util.Constants.PLACE_NOT_FOUND_ERROR;
import static it.unimib.enjoyn.util.Constants.SUGGESTIONS_NOT_FOUND;


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
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;
import java.util.ArrayList;
import java.util.List;
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

        return  new ReverseGeoOptions.Builder(point)
                .limit(1)
                .types(queryTypeList)
                .build();
    }

    public SearchOptions getSearchOption(Point selfLocation) {

        return  new SearchOptions.Builder()
                .limit(4)
                .proximity(selfLocation)
                .build();
    }

    public void getMapSuggestion(String searchBarText, Point selfLocation) {

        startSearchEngine();

        searchEngine.search(searchBarText, getSearchOption(selfLocation), searchCallback);

    }

    public void getMapSearch( List<SearchSuggestion> suggestion) {

        startSearchEngine();



        searchEngine.select(suggestion, searchMultipleSelectionCallback);

    }
    public void getMapReverseSearch( Point point) {

        startSearchEngine();
        searchEngine.search( getReverseOption(point), searchReverseCallback);
    }

    SearchMultipleSelectionCallback searchMultipleSelectionCallback = new SearchMultipleSelectionCallback() {
        @Override
        public void onResult(@NonNull List<SearchSuggestion> list, @NonNull List<SearchResult> searchResult, @NonNull ResponseInfo responseInfo) {
            if (searchResult.size() > 0) {
                mapCallBack.onSuccessSearchFromRemote(searchResult);
            } else {
                mapCallBack.onFailureSearchFromRemote(new Exception(PLACE_NOT_FOUND_ERROR));
            }
        }
        @Override
        public void onError(@NonNull Exception e) {
            mapCallBack.onFailureSearchFromRemote(new Exception(API_MULTIPLE_SEARCH_ERROR));
        }
    };

    SearchSelectionCallback searchCallback = new SearchSelectionCallback() {


        @Override
        public void onSuggestions(@NonNull List<SearchSuggestion> suggestions, @NonNull ResponseInfo responseInfo) {
            if (suggestions.isEmpty()) {
                mapCallBack.onFailureSuggestionFromRemote(new Exception(SUGGESTIONS_NOT_FOUND));

            } else {


                mapCallBack.onSuccessSuggestionFromRemote(suggestions);

            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            mapCallBack.onFailureSuggestionFromRemote(new Exception(SUGGESTIONS_NOT_FOUND));
        }

        @Override
        public void onResults(@NonNull SearchSuggestion searchSuggestion, @NonNull List<SearchResult> list, @NonNull ResponseInfo responseInfo) {

        }

        @Override
        public void onResult(@NonNull SearchSuggestion searchSuggestion, @NonNull SearchResult searchResult, @NonNull ResponseInfo responseInfo) {

        }
    };
    private final SearchCallback searchReverseCallback = new SearchCallback() {

        @Override
        public void onResults(@NonNull List<SearchResult> list, @NonNull ResponseInfo responseInfo) {

            if (list.size() > 0) {
                if(list.size()== 1){
                mapCallBack.onSuccessReverseSearchFromRemote(list.get(0));
                }
            }
            else {
                mapCallBack.onFailureReverseFromRemote(new Exception(NO_PLACES_AVAILABLE));
            }
        }

        @Override
        public void onError(@NonNull Exception e) {

        }
    };
}








