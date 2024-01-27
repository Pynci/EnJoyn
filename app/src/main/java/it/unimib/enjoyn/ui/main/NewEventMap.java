package it.unimib.enjoyn.ui.main;

import static com.mapbox.maps.plugin.animation.CameraAnimationsUtils.getCamera;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import static it.unimib.enjoyn.util.Constants.EMPTY_LOCATION;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
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
import com.mapbox.geojson.Point;


import com.mapbox.maps.CameraOptions;

import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;

import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;

import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;
import com.mapbox.search.ui.view.CommonSearchViewConfiguration;
import com.mapbox.search.ui.view.SearchResultsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.SuggestionListAdapter;
import it.unimib.enjoyn.databinding.FragmentNewEventMapBinding;
import it.unimib.enjoyn.model.EventLocation;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.util.ErrorMessagesUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewEventMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventMap extends Fragment implements PermissionsListener {

    Observer<Result> suggestionObserver;
    Observer<Result> mapSearchObserver;
    private static final String STATE_LOCATION = "location";
    private FragmentNewEventMapBinding fragmentNewEventMapBinding;
    MapView mapView;
    public EventLocation location;
    List<EventLocation> locationList;
    List<Double> distanceList;
     private double distance;
    private SuggestionListAdapter suggestionListAdapter;
    private ListView suggestionListView;

    private EventViewModel eventViewModel;


    Point selfLocation;

    Point eventCoordinates;
    FloatingActionButton positionButton;
    Bitmap bitmap;
    boolean positionChanged = true;
    boolean firstTime = false;

    boolean suggestionClicked = false;

    boolean searchClicked = false;


    private SearchResultsView searchResultsView;
    private TextInputEditText searchBar;
    private PermissionsManager permissionsManager;
    MaterialButton newEventButton;

    List<SearchSuggestion> suggestions;

   private List<SearchResult> searchResultList = null;

   private PointAnnotationManager pointAnnotationManager;



    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if(result){
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.eventRemoveToDo),
                    Snackbar.LENGTH_LONG).show();

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
            if(positionChanged) {
               mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(16.0).build());
               // getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
                NewEventMap.this.selfLocation = point;
               // updateCamera(selfLocation,0.0);
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


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";




    public NewEventMap() {

    }

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
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        if(savedInstanceState != null){
            location = savedInstanceState.getParcelable(STATE_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentNewEventMapBinding = FragmentNewEventMapBinding.inflate(inflater, container, false);
        return fragmentNewEventMapBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (PermissionsManager.areLocationPermissionsGranted(requireActivity())) {

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(requireActivity());

        }


        suggestionListView = view.findViewById(R.id.fragmentNewEventMap_ListView);
        suggestionClicked = false;
        searchClicked = false;
        firstTime = false;
        selfLocation = null;
        eventCoordinates = null;
        mapView = view.findViewById(R.id.newEventmap_mapView);
        positionButton= view.findViewById(R.id.newEventMap_floatingButton_resetInCurrentPosition);
        newEventButton = view.findViewById(R.id.newEventMap_materialButton_eventLocation);
        //TOLTO per barra di ricerca


        searchBar = view.findViewById(R.id.newEventMap_textInputEditText_textSearchBar);
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);

        searchResultsView = view.findViewById(R.id.search_results_view);
        searchResultsView.initialize(new SearchResultsView.Configuration( new CommonSearchViewConfiguration()));

        // immagine 3d scaricata, da usare per creare pin sulla mappa
        bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.location_pin);


        if(location != null && location.getName()!= null){
            eventSelectionPoint();
        } else{
            location = new EventLocation();
        }

        suggestionObserver = result -> {
            if (result.isSuccessful()) {
                searchResultList = new ArrayList<>();
                suggestions = ((Result.MapSuggestionSuccess) result).getData();
                locationList = new ArrayList<>();
                distanceList = new ArrayList<>();
                eventViewModel.getMapSearch(suggestions).observe(getViewLifecycleOwner(), mapSearchObserver);
            }
            else{
                Log.d(this.getClass().getSimpleName(), "SIAMO NEL RAMO ELSE DEL SUGGESTIONOBSERVER TATASDJHASJKBDNAJKHSBDAKNSD");
            }

        };

        mapSearchObserver = result -> {
            if(result.isSuccessful()) {
                searchResultList = ((Result.MapSearchSuccess) result).getData();
                for (int i = 0; i < searchResultList.size(); i++) {
                    locationList.add(new EventLocation());
                    locationList.get(i).setName(searchResultList.get(i).getName());
                    distance = 100 * (Math.sqrt(Math.pow(selfLocation.latitude() - searchResultList.get(i).getCoordinate().latitude(), 2) + Math.pow(selfLocation.longitude() - searchResultList.get(i).getCoordinate().longitude(), 2)));
                    distanceList.add(round(distance, 1));
                }
            }

            suggestionListAdapter = new SuggestionListAdapter(requireContext(), R.layout.suggestion_list_item, locationList, distanceList, (eventLocation, position) -> {

                location.setLatitude(searchResultList.get(position).getCoordinate().latitude());
                location.setLongitude(searchResultList.get(position).getCoordinate().longitude());
                location.setName(searchResultList.get(position).getName());
                firstTime= false;
                searchBar.setText( searchResultList.get(position).getName());
                suggestionListView.setVisibility(View.GONE);
                eventSelectionPoint();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            });
            suggestionListView.setAdapter(suggestionListAdapter);
            suggestionListView.setVisibility(View.VISIBLE);
        };

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(firstTime) {
                    if (s.length() > 3) { //&& !clickSuggestion
                        searchResultList = new ArrayList<>();
                        suggestions = new ArrayList<>();
                        Log.d("TAH", "ASJBDIJAHBNSDHJKABSDHJBASJHDBAUISHEIJQWIEJAJSND");
                        eventViewModel.getMapSuggestion(s.toString(), selfLocation).observe(getViewLifecycleOwner(), suggestionObserver);
                                //}

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


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION );
        }

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, style -> {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(16.0).build());
            LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
            locationComponentPlugin.setEnabled(true);
            LocationPuck2D locationPuck2D = new LocationPuck2D();
            locationPuck2D.setBearingImage(AppCompatResources.getDrawable(getContext(), R.drawable.baseline_place_24));
            locationComponentPlugin.setLocationPuck(locationPuck2D);
            locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
            locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
            getGestures(mapView).addOnMoveListener(onMoveListener);

            searchBar.setOnKeyListener((v, keyCode, event) -> {
                if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode==KeyEvent.KEYCODE_ENTER) )
                {
                    suggestionListView.setVisibility(View.GONE);
                    location.setName(searchResultList.get(0).getName());
                    location.setLatitude(searchResultList.get(0).getCoordinate().latitude());
                    location.setLongitude(searchResultList.get(0).getCoordinate().longitude());
                    eventSelectionPoint();
                    return true;
                }
                return false;
            });



            positionButton.setOnClickListener(v -> {
                // flyToCameraPosition(point);
                updateCamera(selfLocation);
                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                getGestures(mapView).addOnMoveListener(onMoveListener);
                positionButton.hide();
            });

          GesturesUtils.addOnMapClickListener(mapView.getMapboxMap(), point -> {
              location.setLatitude(point.latitude());
              location.setLongitude(point.longitude());
              updateCamera(point);


              pointAnnotationManager.deleteAll();
              PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap)
                      .withPoint(point);
              pointAnnotationManager.create(pointAnnotationOptions);
              eventViewModel.getMapReverseSearch(point).observe(getViewLifecycleOwner(), result -> {
                   if(result.isSuccessful()) {
                       SearchResult reverseSearchResult = ((Result.MapReverseSearchSuccess) result).getData();

                       if (reverseSearchResult.getId().startsWith("ad")) {
                           if (reverseSearchResult.getAddress().getHouseNumber() != null) {
                               location.setName(reverseSearchResult.getName() + " " + reverseSearchResult.getAddress().getHouseNumber());
                           }
                       } else {
                           location.setName(reverseSearchResult.getName());
                       }
                       newEventButton.setText(location.getName());

                   }
              });
              return false;
          });


            newEventButton.setOnClickListener(v -> {

                if(location.getLongitude() != 0.0 && location.getLatitude() != 0.0 && location.getName() != null) {
                    NewEventMapDirections.ActionNewEventMapToNewEventFragment action =
                            NewEventMapDirections.actionNewEventMapToNewEventFragment(location);
                    Navigation.findNavController(view).navigate(action);
                } else {
                    ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
                    Snackbar.make(view, errorMessagesUtil.getMapErrorMessage(EMPTY_LOCATION), Snackbar.LENGTH_LONG).show();
                }
            });

        });
    }

    private void updateCamera(Point point) {
        MapAnimationOptions animationOptions = new MapAnimationOptions.Builder().duration(1000L).build();
        CameraOptions cameraOptions = new CameraOptions.Builder().center(point).zoom(16.0).bearing(0.0).pitch(0.0)
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

    public void eventSelectionPoint(){
        if (location != null && !Objects.equals(location.getName(), "")){
            eventCoordinates = Point.fromLngLat(location.getLongitude(),location.getLatitude());
            pointAnnotationManager.deleteAll();
            PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap)
                    .withPoint(eventCoordinates);
            pointAnnotationManager.create(pointAnnotationOptions);
            updateCamera(eventCoordinates);
            newEventButton.setText(location.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state.
        savedInstanceState.putParcelable(STATE_LOCATION, location);

        // Always call the superclass so it can save the view hierarchy state.
        super.onSaveInstanceState(savedInstanceState);
    }
    public static double round(double n, int decimals) {
        return Math.floor(n * Math.pow(10, decimals)) / Math.pow(10, decimals);
    }
}



