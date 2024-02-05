package it.unimib.enjoyn.repository;


import androidx.lifecycle.MutableLiveData;
import com.mapbox.geojson.Point;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;
import java.util.List;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.source.MapRemoteDataSource;
import it.unimib.enjoyn.util.MapCallBack;

public class MapRepository implements MapCallBack {

    private final MutableLiveData<Result> mapMutableLiveData;

    private final MutableLiveData<Result> mapMutableSearchLiveData;
    private final MutableLiveData<Result> mapMutableReverseSearchLiveData;
    private final MapRemoteDataSource mapRemoteDataSource;


    public MapRepository( MapRemoteDataSource mapRemoteDataSource) {
        this.mapMutableLiveData = new MutableLiveData<>();
        this.mapMutableSearchLiveData = new MutableLiveData<>();
        this.mapMutableReverseSearchLiveData = new MutableLiveData<>();
        this.mapRemoteDataSource = mapRemoteDataSource;
        mapRemoteDataSource.setMapCallBack(this);
    }

    @Override
    public void onSuccessSuggestionFromRemote(List<SearchSuggestion> suggestions) {
        if ( mapMutableLiveData.getValue() != null &&  mapMutableLiveData.getValue().isSuccessful()) {


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


            Result.MapSearchSuccess result = new Result.MapSearchSuccess(searchResult);
            mapMutableSearchLiveData.postValue(result);
        } else {
            Result.MapSearchSuccess result = new Result.MapSearchSuccess(searchResult);
            mapMutableSearchLiveData.postValue(result);
        }
    }

    @Override
    public void onSuccessReverseSearchFromRemote(SearchResult reverseSearchResult) {
        if (  mapMutableReverseSearchLiveData.getValue() != null &&   mapMutableReverseSearchLiveData.getValue().isSuccessful()) {


            Result.MapReverseSearchSuccess result = new Result.MapReverseSearchSuccess(reverseSearchResult);
            mapMutableReverseSearchLiveData.postValue(result);
        } else {
            Result.MapReverseSearchSuccess result = new Result.MapReverseSearchSuccess(reverseSearchResult);
            mapMutableReverseSearchLiveData.postValue(result);
        }
    }

    @Override
    public void onFailureReverseFromRemote(Exception exception) {
                mapMutableReverseSearchLiveData.postValue(new Result.Error(exception.getLocalizedMessage()));
    }
    @Override
    public void onFailureSearchFromRemote(Exception exception) {
        mapMutableSearchLiveData.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    @Override
    public void onFailureSuggestionFromRemote(Exception exception) {
        mapMutableLiveData.postValue(new Result.Error(exception.getLocalizedMessage()));
    }

    public MutableLiveData<Result> fetchMapSu(String searchBarText, Point selfLocation) {

        mapRemoteDataSource.getMapSuggestion(searchBarText, selfLocation);

        return mapMutableLiveData;
    }


    public MutableLiveData<Result> fetchMapSearch( List<SearchSuggestion> suggestion) {

        mapRemoteDataSource.getMapSearch(suggestion);

        return mapMutableSearchLiveData;
    }

    public MutableLiveData<Result> fetchMapReverseSearch( Point point) {

        mapMutableReverseSearchLiveData.setValue(null);
        mapRemoteDataSource.getMapReverseSearch(point);

        return mapMutableReverseSearchLiveData;
    }
}
