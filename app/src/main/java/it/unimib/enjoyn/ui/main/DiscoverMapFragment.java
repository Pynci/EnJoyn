package it.unimib.enjoyn.ui.main;

import static com.mapbox.maps.plugin.animation.CameraAnimationsUtils.getCamera;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import static it.unimib.enjoyn.util.Constants.VIEW_MODEL_ERROR;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.EdgeInsets;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;
import com.mapbox.maps.plugin.annotation.Annotation;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.OnAnnotationClickListener;
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchEngineSettings;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.autocomplete.PlaceAutocomplete;
import com.mapbox.search.common.AsyncOperationTask;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;
import com.mapbox.search.ui.view.CommonSearchViewConfiguration;
import com.mapbox.search.ui.view.SearchResultsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.SuggestionListAdapter;
import it.unimib.enjoyn.databinding.FragmentDiscoverMapBinding;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.EventLocation;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.util.ErrorMessagesUtil;
import it.unimib.enjoyn.util.SnackbarBuilder;

public class DiscoverMapFragment extends Fragment implements PermissionsListener {

    Observer<Result> suggestionObserver;
    Observer<Result> mapSearchObserver;
    EventViewModel eventViewModel;

    private FragmentDiscoverMapBinding fragmentDiscoverMapBinding;

    List<Event> eventList;
    Event event;
    MapView mapView;
    //public EventLocation location;
    List<EventLocation> locationList;
    Point selfLocation;
    private SearchEngine searchEngine;
    Bitmap bitmap;
    List<Double> distanceList;
    private double distance;
    private double distanceSuggestionLatitude;
    private double distanceSuggestionsLongitude;
    Point eventCoordinates;
    FloatingActionButton positionButton;
    boolean positionChanged = true;
    boolean firstTime = false;
    boolean suggestionClicked = false;
    boolean searchClicked = false;
    List<SearchSuggestion> suggestions;
    private List<SearchResult> searchResultList = null;
    private ListView suggestionListView;
    private SuggestionListAdapter suggestionListAdapter;
    //private SearchResultsView searchResultsView;
    //private PlaceAutocomplete placeAutocomplete;
    private TextInputEditText searchBar;
    private PermissionsManager permissionsManager;
    private PointAnnotationManager pointAnnotationManager;
    private AsyncOperationTask searchRequestTask;
    CardView eventItem;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result){
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        getString(R.string.eventRemoveToDo),
                        Snackbar.LENGTH_LONG).show();

            }
        }
    });

    public DiscoverMapFragment() {
        // Required empty public constructor
    }

    public static DiscoverMapFragment newInstance() {
        return new DiscoverMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentDiscoverMapBinding = FragmentDiscoverMapBinding.inflate(inflater, container, false);
        return fragmentDiscoverMapBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (PermissionsManager.areLocationPermissionsGranted(requireActivity())) {

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(requireActivity());

        }
        searchEngine = SearchEngine.createSearchEngineWithBuiltInDataProviders(
                new SearchEngineSettings(getString(R.string.mapbox_access_token))
        );

        final SearchOptions options = new SearchOptions.Builder()
                .limit(4)
                .proximity(selfLocation)
                .build();


        suggestionListView = view.findViewById(R.id.fragmentDiscoverMap_listView);
        suggestionClicked = false;
        searchClicked = false;
        firstTime = false;
        positionButton = view.findViewById(R.id.fragmentDiscoverMap_FloatingActionButton);
        selfLocation = null;
        searchBar = view.findViewById(R.id.fragmentDiscoverMap_TextInputEditText_searchBar);
        mapView = view.findViewById(R.id.fragmentDiscoverMap_mapView);
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);
        //placeAutocomplete = PlaceAutocomplete.create(getString(R.string.mapbox_access_token));
        fragmentDiscoverMapBinding.fragmentDiscoverMapSearchResultView.initialize(new SearchResultsView.Configuration( new CommonSearchViewConfiguration()));


        // immagine 3d scaricata, da usare per creare pin sulla mappa
        //bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.location_pin);
        // Dichiarazione della variabile annotationApi
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION );
        }

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(firstTime) {
                    if (s.length() > 3) { //&& !clickSuggestion
                        searchRequestTask = searchEngine.search(s.toString(), options, searchCallback);
                        suggestionListView.setVisibility(View.VISIBLE);
                    } else {
                        suggestionListView.setVisibility(View.GONE);
                        //clickSuggestion = false;
                    }
                } else{
                    firstTime=true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(16.0).build());
                LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
                locationComponentPlugin.setEnabled(true);
                LocationPuck2D locationPuck2D = new LocationPuck2D();
                locationPuck2D.setBearingImage(AppCompatResources.getDrawable(getContext(), R.drawable.baseline_place_24));
                locationComponentPlugin.setLocationPuck(locationPuck2D);
                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                getGestures(mapView).addOnMoveListener(onMoveListener);

                /*fragmentDiscoverMapBinding.fragmentDiscoverMapTextInputLayoutSearchBar.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            fragmentDiscoverMapBinding.fragmentDiscoverMapListView.setVisibility(View.GONE);
                            searchClicked = true;

                            return true;
                        }
                        return false;
                    }
                });*/
                positionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // flyToCameraPosition(point);
                        updateCamera(selfLocation, 0.0);
                        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                        getGestures(mapView).addOnMoveListener(onMoveListener);


                        positionButton.hide();

                    }
                });

                GesturesUtils.addOnMapClickListener(mapView.getMapboxMap(), new OnMapClickListener() {
                    @Override
                    public boolean onMapClick(@NonNull Point point) {
                        eventItem.setVisibility(View.GONE);
                        return false;
                    }
                });

            }
        });


        bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.location_pin);
        //Todo logica eventi
        eventViewModel.getEvent(Long.parseLong("0")).observe(getViewLifecycleOwner(), result -> {

            if(result.isSuccessful()){
                this.eventList.clear();
                this.eventList.addAll(((Result.EventSuccess) result).getData().getEventList());
            } else {
                ErrorMessagesUtil errorMessagesUtil =
                        new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.
                                getEventErrorMessage(VIEW_MODEL_ERROR),
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        Point point = Point.fromLngLat(-122.08497461176863, 37.42241542518461);
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER)
                .withPoint(point)
                .withIconImage(bitmap);

        if (pointAnnotationManager != null) {
            pointAnnotationManager.create(pointAnnotationOptions);
        }

        Point point2 = Point.fromLngLat(-122.08564492453274, 37.42158123915911);
        pointAnnotationOptions.withTextAnchor(TextAnchor.CENTER)
                .withPoint(point2)
                .withIconImage(bitmap);

        if (pointAnnotationManager != null) {
            pointAnnotationManager.create(pointAnnotationOptions);
        }

        Point point3 = Point.fromLngLat(11.436340722694551,46.28347051230523);
        pointAnnotationOptions.withTextAnchor(TextAnchor.CENTER)
                .withPoint(point3)
                .withIconImage(bitmap);

        if (pointAnnotationManager != null) {
            pointAnnotationManager.create(pointAnnotationOptions);
        }

        eventItem = view.findViewById(R.id.fragmentDiscoverMap_cardView_eventItem);


        if (pointAnnotationManager != null) {
            pointAnnotationManager.addClickListener(new OnPointAnnotationClickListener() {
                @Override
                public boolean onAnnotationClick(PointAnnotation annotation) {
                    /*for(int i = 0; i < eventList.size(); i++){

                    }*/
                    event = eventList.get((int)annotation.getId());
                    eventItem.setVisibility(View.VISIBLE);

                    fragmentDiscoverMapBinding.eventListItemTextViewEventTitle.setText(event.getTitle());
                    fragmentDiscoverMapBinding.eventListItemTextViewDate.setText(event.getDate());
                    fragmentDiscoverMapBinding.eventListItemTextViewTime.setText(event.getTime());
                    fragmentDiscoverMapBinding.eventListItemTextViewPlace.setText(event.getPlace());
                    //calcolare distanza
                    //fragmentDiscoverMapBinding.eventListItemTextViewPeopleNumber.setText(event.getPeopleNumber());
                    //fragmentDiscoverMapBinding.eventListItemImageViewEventImage.setImageURI(event.getImageUrl());
                    //mettere immagine meteo


                    eventItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DiscoverFragmentDirections.ActionDiscoverToDiscoverSingleEvent action = DiscoverFragmentDirections.actionDiscoverToDiscoverSingleEvent(event);
                            Navigation.findNavController(view).navigate(action);

                        }
                    });

                    //DiscoverFragmentDirections.ActionDiscoverToDiscoverSingleEvent action = DiscoverFragmentDirections.actionDiscoverToDiscoverSingleEvent(event);
                    //Navigation.findNavController(view).navigate(action);
                    annotation.getPoint().latitude();
                    annotation.getPoint().longitude();
                    Snackbar.make(view, "va point: LAT: "+annotation.getPoint().latitude()+" LONG: "+annotation.getPoint().longitude()+ " ID: "+annotation.getId(), Snackbar.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

    }




    private final SearchSelectionCallback searchCallback = new SearchSelectionCallback() {

        @Override
        public void onSuggestions(@NonNull List<SearchSuggestion> suggestions, @NonNull ResponseInfo responseInfo) {
            if (suggestions.isEmpty()) {
                Log.i("SearchApiExample", "No suggestions found");
            } else {
                Log.i("SearchApi", "Search suggestions: " + suggestions + "\nSelecting first...");
               /* if(searchClicked)
                searchRequestTask = searchEngine.select(suggestions.get(0), this);*/
                //searchResultsView.set(suggestions);


                locationList = new ArrayList<>();
                distanceList = new ArrayList<>();

                for(int i = 0; i<suggestions.size(); i++){
                    locationList.add(new EventLocation());
                    locationList.get(i).setName(suggestions.get(i).getName());
                    searchRequestTask = searchEngine.select(suggestions.get(i), searchCallback);
                    distance = 100*(Math.sqrt(Math.pow(selfLocation.latitude()-distanceSuggestionLatitude,2) + Math.pow(selfLocation.longitude() -distanceSuggestionsLongitude,2)));
                    distanceList.add(round(distance,1));
                    //locationList.get(i).setLatitude(suggestions.get(i).getRequestOptions().getOptions());
                    //locationList.get(i).setLongitude(suggestions.get(i).getRequestOptions().getOptions().getProximity().longitude());
                }


                suggestionListAdapter = new SuggestionListAdapter(requireContext(), R.layout.suggestion_list_item, locationList, distanceList,  new SuggestionListAdapter.OnItemClickListener() {
                    @Override
                    public void onSuggestionItemClick(EventLocation eventLocation, int position) {
                        suggestionClicked = true;
                        searchRequestTask = searchEngine.select(suggestions.get(position), searchCallback);

                        searchBar.setText(suggestions.get(position).getName());
                        suggestionListView.setVisibility(View.GONE);

                        //clickSuggestion = true;
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    }
                });
                suggestionListView.setAdapter(suggestionListAdapter);
            }
            searchBar.setOnKeyListener(new View.OnKeyListener(){
                public boolean onKey(View v, int keyCode, KeyEvent event){
                    if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode==KeyEvent.KEYCODE_ENTER) )
                    {
                        searchClicked = true;
                        searchRequestTask = searchEngine.select(suggestions.get(0), searchCallback);
                        suggestionListView.setVisibility(View.GONE);

                        return true;
                    }
                    return false;
                }
            });
        }

        @Override
        public void onResult(@NonNull SearchSuggestion suggestion, @NonNull SearchResult result, @NonNull ResponseInfo info) {
            Log.i("SearchApie", "Search result: " + result);
            if(suggestionClicked || searchClicked ) {
                eventSelectionPoint(result);
                suggestionClicked = false;
                searchClicked = false;
            }
            distanceSuggestionLatitude = result.getCoordinate().latitude();
            distanceSuggestionsLongitude = result.getCoordinate().longitude();

            //Log.i("SearchApiExample", "Search result: " + location.getName());
            //Log.i("SearchApiExample", "Search result: " + location.getLatitudeToString());
            //Log.i("SearchApiExample", "Search result: " + location.getLongitude());
        }

        @Override
        public void onResults(@NonNull SearchSuggestion suggestion, @NonNull List<SearchResult> results, @NonNull ResponseInfo responseInfo) {
            Log.i("SearchApiExample", "Category search results: " + results);
        }

        @Override
        public void onError(@NonNull Exception e) {
            Log.i("SearchApiExample", "Search error: ", e);
        }
    };



    private final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener = new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().bearing(v).build());
        }
    };
    private final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            if(positionChanged) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(16.0).build());
                // getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
                DiscoverMapFragment.this.selfLocation = point;
                positionChanged = false;
            }
            else{
                getLocationComponent(mapView).removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                positionChanged = true;
            }
        }
    };
    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            getLocationComponent(mapView).removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
            getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
            getGestures(mapView).removeOnMoveListener(onMoveListener);
            positionButton.show();
        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }
    public static double round(double n, int decimals) {
        return Math.floor(n * Math.pow(10, decimals)) / Math.pow(10, decimals);
    }

    public void eventSelectionPoint(SearchResult result){
        if (result != null){
            eventCoordinates = Point.fromLngLat(result.getCoordinate().longitude(),result.getCoordinate().latitude());
            updateCamera(eventCoordinates, 0.0);
        }
    }

    private void updateCamera(Point point, Double bearing) {
        MapAnimationOptions animationOptions = new MapAnimationOptions.Builder().duration(1000L).build();
        CameraOptions cameraOptions = new CameraOptions.Builder().center(point).zoom(16.0).bearing(bearing).pitch(0.0)
                .padding(new EdgeInsets(1000.0, 0.0, 0.0, 0.0)).build();

        getCamera(mapView).easeTo(cameraOptions, animationOptions);
    }
    @Override
    public void onDestroy() {
        if (searchRequestTask != null) {
            searchRequestTask.cancel();
        }
        super.onDestroy();
    }

}
