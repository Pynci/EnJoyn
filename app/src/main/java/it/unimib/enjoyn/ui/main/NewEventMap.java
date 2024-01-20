package it.unimib.enjoyn.ui.main;

import static com.mapbox.maps.plugin.animation.CameraAnimationsUtils.getCamera;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import static it.unimib.enjoyn.util.Constants.EMPTY_LOCATION;

import android.Manifest;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.maps.EdgeInsets;
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;
import com.mapbox.geojson.Point;


import com.mapbox.maps.CameraOptions;

import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;

import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.ReverseGeoOptions;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchEngineSettings;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.autocomplete.PlaceAutocomplete;
import com.mapbox.search.common.AsyncOperationTask;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;
import com.mapbox.search.ui.adapter.autocomplete.PlaceAutocompleteUiAdapter;
import com.mapbox.search.ui.view.CommonSearchViewConfiguration;
import com.mapbox.search.ui.view.SearchResultsView;
import com.mapbox.search.ui.adapter.engines.SearchEngineUiAdapter;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.databinding.FragmentNewEventBinding;
import it.unimib.enjoyn.databinding.FragmentNewEventMapBinding;
import it.unimib.enjoyn.model.EventLocation;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.util.ErrorMessagesUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEventMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventMap extends Fragment implements PermissionsListener {
    private FragmentNewEventMapBinding fragmentNewEventMapBinding;
    private FragmentNewEventBinding fragmentNewEventBinding;
    MapView mapView;
    public EventLocation location;

    Point selfLocation;

    Point eventLocation;
    FloatingActionButton positionButton;
    MapboxMap mapboxMap;
    Bitmap bitmap;
    int i=0;

    private PlaceAutocomplete placeAutocomplete;

    private SearchResultsView searchResultsView;

    private PlaceAutocompleteUiAdapter placeAutocompleteUiAdapter;
    private TextInputEditText searchBar;
    private boolean ignoreNextQueryUpdate = false;
    private PermissionsManager permissionsManager;
    private SearchEngineUiAdapter searchEngineUiAdapter;
    private SearchView searchView;
    MaterialButton newEventButton;

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

    private final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener = new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().bearing(v).build());
        }
    };
    private final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            if(i==0) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(16.0).build());
                getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
                NewEventMap.this.selfLocation = point;
                i=1;

            }
            else{
                getLocationComponent(mapView).removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                getGestures(mapView).removeOnMoveListener(onMoveListener);
                i=0;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewEventMap() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newEventMap.
     */
    // TODO: Rename and change types and number of parameters
    public static NewEventMap newInstance(String param1, String param2) {
        NewEventMap fragment = new NewEventMap();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentNewEventMapBinding = FragmentNewEventMapBinding.inflate(inflater, container, false);
        return fragmentNewEventMapBinding.getRoot();
        // return inflater.inflate(R.layout.fragment_new_event_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (PermissionsManager.areLocationPermissionsGranted(requireActivity())) {
            // Permission-sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(requireActivity());
        }

        searchEngine = SearchEngine.createSearchEngineWithBuiltInDataProviders(
                new SearchEngineSettings(getString(R.string.mapbox_access_token))
        );

        final SearchOptions options = new SearchOptions.Builder()
                .limit(5)
                .build();



        selfLocation = null;
        eventLocation = null;
        mapView = view.findViewById(R.id.mapView);
        positionButton= view.findViewById(R.id.newEventMap_floatingButton_resetInCurrentPosition);
        newEventButton = view.findViewById(R.id.newEventMap_materialButton_eventLocation);
        //TOLTO per barra di ricerca
        placeAutocomplete = PlaceAutocomplete.create(getString(R.string.mapbox_access_token));

        searchBar = view.findViewById(R.id.newEventMap_textInputEditText_textSearchBar);
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);

        searchResultsView = view.findViewById(R.id.search_results_view);
        searchResultsView.initialize(new SearchResultsView.Configuration( new CommonSearchViewConfiguration()));
        //TOLTO per barra di ricerca
        placeAutocompleteUiAdapter = new PlaceAutocompleteUiAdapter(searchResultsView, placeAutocomplete, LocationEngineProvider.getBestLocationEngine(getContext()));

        // immagine 3d scaricata, da usare per creare pin sulla mappa
        bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.location_pin);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3){
                    searchRequestTask = searchEngine.search(s.toString(), options, searchCallback);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION );
        }

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

                searchBar.setOnKeyListener(new View.OnKeyListener(){
                    public boolean onKey(View v, int keyCode, KeyEvent event){
                        if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER))
                        {
                            if (location != null && location.getName() != ""){
                                eventLocation = Point.fromLngLat(location.getLongitude(),location.getLatitude());


                                pointAnnotationManager.deleteAll();
                                PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap)
                                        .withPoint(eventLocation);
                                pointAnnotationManager.create(pointAnnotationOptions);
                                updateCamera(eventLocation, 0.0);
                                newEventButton.setText(location.getName());

                                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                        "EEEEEEEEEEEEEEEEEEEEEEEEEEEEOOOOOOOOOOOOOOO",
                                        Snackbar.LENGTH_LONG).show();
                            }
                            return true;
                        }
                        return false;
                    }
                });

                /*
                OfflineSearchEngine offlineSearchEngine = OfflineSearchEngine.create(
                        new OfflineSearchEngineSettings(getString(R.string.mapbox_access_token))
                );
                searchEngineUiAdapter = new SearchEngineUiAdapter(searchResultsView, searchEngine, offlineSearchEngine, null, null);

                searchEngineUiAdapter.setSearchMode(SearchMode.AUTO);
                searchEngineUiAdapter.addSearchListener(new SearchEngineUiAdapter.SearchListener() {
                    @Override
                    public void onSuggestionsShown(@NonNull List<SearchSuggestion> list, @NonNull ResponseInfo responseInfo) {
                    }

                    @Override
                    public void onSearchResultsShown(@NonNull SearchSuggestion searchSuggestion, @NonNull List<SearchResult> list, @NonNull ResponseInfo responseInfo) {
                    }

                    @Override
                    public void onOfflineSearchResultsShown(@NonNull List<OfflineSearchResult> list, @NonNull OfflineResponseInfo offlineResponseInfo) {
                    }

                    @Override
                    public boolean onSuggestionSelected(@NonNull SearchSuggestion searchSuggestion) {
                        return false;
                    }

                    @Override
                    public void onSearchResultSelected(@NonNull SearchResult searchResult, @NonNull ResponseInfo responseInfo) {
                        closeSearchView();

                    }

                    @Override
                    public void onOfflineSearchResultSelected(@NonNull OfflineSearchResult offlineSearchResult, @NonNull OfflineResponseInfo offlineResponseInfo) {
                    }

                    @Override
                    public void onError(@NonNull Exception e) {
                    }

                    @Override
                    public void onHistoryItemClick(@NonNull HistoryRecord historyRecord) {
                    }

                    @Override
                    public void onPopulateQueryClick(@NonNull SearchSuggestion searchSuggestion, @NonNull ResponseInfo responseInfo) {
                        if(searchView != null){
                            searchView.setQuery(searchSuggestion.getName(), true);
                        }
                    }

                    @Override
                    public void onFeedbackItemClick(@NonNull ResponseInfo responseInfo) {
                    }
                });
                */

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
                location = new EventLocation();


              GesturesUtils.addOnMapClickListener(mapView.getMapboxMap(), new OnMapClickListener() {
                   @Override
                   public boolean onMapClick(@NonNull Point point) {
                       location.setLatitude(point.latitude());
                       location.setLongitude(point.longitude());
                       updateCamera(point, 0.0);
                       //todo mettere logica per pin sulla mappa da testare (riga 325)

                       pointAnnotationManager.deleteAll();
                       PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap)
                               .withPoint(point);
                       pointAnnotationManager.create(pointAnnotationOptions);

                       final ReverseGeoOptions optionsReverse = new ReverseGeoOptions.Builder(point)
                               .limit(1)
                               .build();
                       searchRequestTask = searchEngine.search(optionsReverse, searchReverseCallback);


                       //prova(point);
                       return false;
                   }
               });


                newEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*Bundle bundle = new Bundle();
                        bundle.putParcelable("LOCATION",location);
                        getParentFragmentManager().setFragmentResult("LOCATION_BUNDLE", bundle);
                         */
                        if(location.getLongitude() != 0.0 && location.getLatitude() != 0.0 && location.getName() != null) {
                            NewEventMapDirections.ActionNewEventMapToNewEventFragment action =
                                    NewEventMapDirections.actionNewEventMapToNewEventFragment(location);
                            Navigation.findNavController(view).navigate(action);
                        } else {
                            ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
                            Snackbar.make(view, errorMessagesUtil.getMapErrorMessage(EMPTY_LOCATION), Snackbar.LENGTH_LONG).show();
                        }
                        //getParentFragmentManager().popBackStackImmediate();
                    }
                });

            }
        });




    }



    private void updateCamera(Point point, Double bearing) {
        MapAnimationOptions animationOptions = new MapAnimationOptions.Builder().duration(1000L).build();
        CameraOptions cameraOptions = new CameraOptions.Builder().center(point).zoom(16.0).bearing(bearing).pitch(0.0)
                .padding(new EdgeInsets(1000.0, 0.0, 0.0, 0.0)).build();

        getCamera(mapView).easeTo(cameraOptions, animationOptions);
    }





    @Override
    public void onExplanationNeeded(@NonNull List<String> list) {

    }

    @Override
    public void onPermissionResult(boolean b) {

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissionsManager != null) {
            permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private SearchEngine searchEngine;
    private AsyncOperationTask searchRequestTask;

    private final SearchSelectionCallback searchCallback = new SearchSelectionCallback() {

        @Override
        public void onSuggestions(@NonNull List<SearchSuggestion> suggestions, @NonNull ResponseInfo responseInfo) {
            if (suggestions.isEmpty()) {
                Log.i("SearchApiExample", "No suggestions found");
            } else {
                Log.i("SearchApiExample", "Search suggestions: " + suggestions + "\nSelecting first...");
                searchRequestTask = searchEngine.select(suggestions.get(0), this);
                //searchResultsView.set(suggestions);
            }
        }

        @Override
        public void onResult(@NonNull SearchSuggestion suggestion, @NonNull SearchResult result, @NonNull ResponseInfo info) {
            Log.i("SearchApiExample", "Search result: " + result);
            if (location != null){
                location.setLatitude(result.getCoordinate().latitude());
                location.setLongitude(result.getCoordinate().longitude());
                location.setName(result.getName());
            }
            location.setLatitude(result.getCoordinate().latitude());
            location.setLongitude(result.getCoordinate().longitude());
            location.setName(result.getName());
            Log.i("SearchApiExample", "Search result: " + location.getName());
            Log.i("SearchApiExample", "Search result: " + location.getLatitudeToString());
            Log.i("SearchApiExample", "Search result: " + location.getLongitude());
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

    @Override
    public void onDestroy() {
        if (searchRequestTask != null) {
            searchRequestTask.cancel();
        }
        super.onDestroy();
    }

    private final SearchCallback searchReverseCallback = new SearchCallback() {

        @Override
        public void onResults(@NonNull List<SearchResult> results, @NonNull ResponseInfo responseInfo) {
            if (results.isEmpty()) {
                Log.i("SearchApiExample", "No reverse geocoding results");
            } else {
                Log.i("SearchApiExample", "Reverse geocoding results: " + results+ " "+ results.get(0).getName());
                location.setName(results.get(0).getName());
                newEventButton.setText(location.getName());


            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            Log.i("SearchApiExample", "Reverse geocoding error", e);
        }
    };
    }



