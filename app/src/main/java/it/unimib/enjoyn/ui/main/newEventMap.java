package it.unimib.enjoyn.ui.main;

import static com.mapbox.maps.plugin.animation.CameraAnimationsUtils.getCamera;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

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


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import com.mapbox.common.Cancelable;
import com.mapbox.geojson.Point;


import com.mapbox.maps.CameraOptions;

import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.ViewAnnotationOptions;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin;
import com.mapbox.maps.plugin.animation.CameraAnimationsUtils;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;

import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;
import com.mapbox.maps.viewannotation.ViewAnnotationManager;
import com.mapbox.search.autocomplete.PlaceAutocomplete;
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion;
import com.mapbox.search.ui.adapter.autocomplete.PlaceAutocompleteUiAdapter;
import com.mapbox.search.ui.view.CommonSearchViewConfiguration;
import com.mapbox.search.ui.view.SearchResultsView;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.databinding.FragmentNewEventBinding;
import it.unimib.enjoyn.databinding.FragmentNewEventMapBinding;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newEventMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newEventMap extends Fragment implements  PermissionsListener {
    private FragmentNewEventMapBinding fragmentNewEventMapBinding;
    private FragmentNewEventBinding fragmentNewEventBinding;
    MapView mapView;
    EventLocation location;

    Point point;
    FloatingActionButton positionButton;
    MapboxMap mapboxMap;

    private PlaceAutocomplete placeAutocomplete;

    private SearchResultsView searchResultsView;

    private PlaceAutocompleteUiAdapter placeAutocompleteUiAdapter;
    private TextInputEditText searchBar;
    private boolean ignoreNextQueryUpdate = false;
    private PermissionsManager permissionsManager;

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
        mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(16.0).build());
        getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
        newEventMap.this.point = point;

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

    public newEventMap() {
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
    public static newEventMap newInstance(String param1, String param2) {
        newEventMap fragment = new newEventMap();
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
        point = null;
        mapView = view.findViewById(R.id.mapView);
        positionButton= view.findViewById(R.id.newEventMap_floatingButton_resetInCurrentPosition);
        MaterialButton newEventButton = view.findViewById(R.id.newEventMap_materialButton_eventLocation);

        placeAutocomplete = PlaceAutocomplete.create(getString(R.string.mapbox_access_token));

        searchBar = view.findViewById(R.id.newEventMap_textInputEditText_textSearchBar);
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);

        searchResultsView = view.findViewById(R.id.search_results_view);
        searchResultsView.initialize(new SearchResultsView.Configuration( new CommonSearchViewConfiguration()));
        placeAutocompleteUiAdapter = new PlaceAutocompleteUiAdapter(searchResultsView, placeAutocomplete, LocationEngineProvider.getBestLocationEngine(getContext()));
       // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (ignoreNextQueryUpdate) {
                    ignoreNextQueryUpdate = false;
                } else {
                    placeAutocompleteUiAdapter.search(s.toString(), new Continuation<Unit>() {
                        @NonNull
                        @Override
                        public CoroutineContext getContext() {
                            return EmptyCoroutineContext.INSTANCE;
                        }

                        @Override
                        public void resumeWith(@NonNull Object o) {
                           requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchResultsView.setVisibility(View.VISIBLE);
                                }
                        });
                        }
                    });

                }
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

                positionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // flyToCameraPosition(point);
                        updateCamera(point, 0.0);
                        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                        getGestures(mapView).addOnMoveListener(onMoveListener);

                        positionButton.hide();

                    }
                });
                location = new EventLocation();


             /*  GesturesUtils.addOnMapClickListener(mapView.getMapboxMap(), new OnMapClickListener() {
                   @Override
                   public boolean onMapClick(@NonNull Point point) {
                       location.setLatitude(point.latitude());
                       location.setLongitude(point.longitude());
                       newEventButton.setText(location.getLatitudeToString());
                       //prova(point);
                       return false;
                   }
               });*/
            }
        });

        //TODO con spostamento a ricerca
        placeAutocompleteUiAdapter.addSearchListener(new PlaceAutocompleteUiAdapter.SearchListener() {
            @Override
            public void onSuggestionsShown(@NonNull List<PlaceAutocompleteSuggestion> list) {

            }

            @Override
            public void onSuggestionSelected(@NonNull PlaceAutocompleteSuggestion placeAutocompleteSuggestion) {
                ignoreNextQueryUpdate = true;
                searchBar.setText(placeAutocompleteSuggestion.getName());
                searchResultsView.setVisibility(View.GONE);
                //todo PIN sulla mappa
             //   PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap).withPoint(placeAutocompleteSuggestion.getCoordinate())
                location.setLongitude(placeAutocompleteSuggestion.getCoordinate().longitude());
                location.setLatitude(placeAutocompleteSuggestion.getCoordinate().latitude());
                location.setName(placeAutocompleteSuggestion.getName());
                pointAnnotationManager.deleteAll();
                PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(String.valueOf(R.drawable.baseline_add_location_24))
                        .withPoint(placeAutocompleteSuggestion.getCoordinate());
                pointAnnotationManager.create(pointAnnotationOptions);
                updateCamera(placeAutocompleteSuggestion.getCoordinate(), 0.0);
                newEventButton.setText(location.getName());
            }

            @Override
            public void onPopulateQueryClick(@NonNull PlaceAutocompleteSuggestion placeAutocompleteSuggestion) {

            }

            @Override
            public void onError(@NonNull Exception e) {

            }
        });






    }
    private void updateCamera(Point point, Double bearing) {
        MapAnimationOptions animationOptions = new MapAnimationOptions.Builder().duration(1000L).build();
        CameraOptions cameraOptions = new CameraOptions.Builder().center(point).zoom(18.0).bearing(bearing).pitch(45.0)
                .padding(new EdgeInsets(1000.0, 0.0, 0.0, 0.0)).build();

        getCamera(mapView).easeTo(cameraOptions, animationOptions);
    }

    private void flyToCameraPosition(Point point) {
        Point cameraCenterCoordinates = com.mapbox.geojson.Point.fromLngLat(8.191926, 45.464098);
        final CameraAnimationsPlugin camera = getCamera(mapView);
        final Cancelable cancelable = (Cancelable) camera.easeTo(
                new CameraOptions.Builder()
                        .center(point)
                        .bearing(0.0)
                        .pitch(0.0)
                        .zoom(13.0)
                        .build(),
                new MapAnimationOptions.Builder().duration(800).build());


    }


  /*  private void prova(Point point) {
        ViewAnnotationManager viewAnnotationManager = fragmentNewEventMapBinding.mapView.getViewAnnotationManager();
        AnnotatedFeature annotatedFeature = new AnnotatedFeature(point);
        ViewAnnotationOptions viewAnnotationOptions = new ViewAnnotationOptions.Builder().annotatedFeature(annotatedFeature)
                // View annotation is placed at the specific geo coordinate
                .build();
        View viewAnnotation = viewAnnotationManager.addViewAnnotation(
                // Specify the layout resource id
                R.layout.view_annotation_mapbox,
                // Set any view annotation options
                viewAnnotationOptions
        );
    }*/

    @Override
    public void onExplanationNeeded(@NonNull List<String> list) {

    }

    @Override
    public void onPermissionResult(boolean b) {

    }

    public void addOnMapClickListener(OnMapClickListener listener) {
        listener.onMapClick(point);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissionsManager != null) {
            permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }






    }



