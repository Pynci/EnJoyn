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
import android.graphics.Color;
import android.os.Bundle;

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
import android.widget.ImageView;
import android.widget.ListView;

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
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
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

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.SuggestionListAdapter;
import it.unimib.enjoyn.databinding.FragmentDiscoverMapBinding;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.EventLocation;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.Weather;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.util.ErrorMessagesUtil;

public class DiscoverMapFragment extends Fragment implements PermissionsListener {

    Observer<Result> suggestionObserver;
    Observer<Result> mapSearchObserver;
    EventViewModel eventViewModel;

    private FragmentDiscoverMapBinding fragmentDiscoverMapBinding;

    List<Event> eventList;
    Event event;
    MapView mapView;
    List<EventLocation> locationList;
    Point selfLocation;
    Bitmap bitmap;
    List<Double> distanceList;
    private double distance;
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
    private TextInputEditText searchBar;
    private Weather weatherAPIdata;
    CardView eventItem;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if(result){
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.eventRemoveToDo),
                    Snackbar.LENGTH_LONG).show();

        }
    });

    public DiscoverMapFragment() {}

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentDiscoverMapBinding = FragmentDiscoverMapBinding.inflate(inflater, container, false);
        return fragmentDiscoverMapBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!PermissionsManager.areLocationPermissionsGranted(requireActivity())) {
            PermissionsManager permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(requireActivity());
        }

        suggestionListView = view.findViewById(R.id.fragmentDiscoverMap_listView);
        suggestionClicked = false;
        searchClicked = false;
        firstTime = false;
        positionButton = view.findViewById(R.id.fragmentDiscoverMap_FloatingActionButton);
        selfLocation = null;
        searchBar = view.findViewById(R.id.fragmentDiscoverMap_TextInputEditText_searchBar);
        mapView = view.findViewById(R.id.fragmentDiscoverMap_mapView);

        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);
        fragmentDiscoverMapBinding.fragmentDiscoverMapSearchResultView.initialize(new SearchResultsView.Configuration( new CommonSearchViewConfiguration()));

        suggestionObserver = result -> {
            if (result.isSuccessful()) {
                searchResultList = new ArrayList<>();
                suggestions = ((Result.MapSuggestionSuccess) result).getData();
                locationList = new ArrayList<>();
                distanceList = new ArrayList<>();
                eventViewModel.getMapSearch(suggestions).observe(getViewLifecycleOwner(), mapSearchObserver);
            }
            else{
                Snackbar.make(view, ((Result.Error) result).getMessage() , Snackbar.LENGTH_SHORT).show();
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
            }else{
                Snackbar.make(view, ((Result.Error) result).getMessage() , Snackbar.LENGTH_SHORT).show();
            }

            suggestionListAdapter = new SuggestionListAdapter(requireContext(), R.layout.suggestion_list_item, locationList, distanceList, (eventLocation, position) -> {

                firstTime= false;
                searchBar.setText( searchResultList.get(position).getName());
                suggestionListView.setVisibility(View.GONE);
                eventSelectionPoint(searchResultList.get(position));
                if(getContext()!= null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if(getView() != null)
                        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
            });
            suggestionListView.setAdapter(suggestionListAdapter);
            suggestionListView.setVisibility(View.VISIBLE);
        };

        if(getContext() != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                suggestionListView.setVisibility(View.GONE);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(firstTime) {
                    if (s.length() > 3) {
                        searchResultList = new ArrayList<>();
                        suggestions = new ArrayList<>();
                        eventViewModel.getMapSuggestion(s.toString(), selfLocation).observe(getViewLifecycleOwner(), suggestionObserver);
                        eventItem.setVisibility(View.GONE);
                    } else {
                        suggestionListView.setVisibility(View.GONE);
                    }
                } else{
                    firstTime=true;
                    suggestionListView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
                    return true;
                }
                return false;
            });

            positionButton.setOnClickListener(v -> {
                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                getGestures(mapView).addOnMoveListener(onMoveListener);
                searchBar.setText("");
            });

        });


        bitmap = BitmapFactory.decodeResource(view.getResources(), R.drawable.location_pin);

        eventViewModel.getEvent().observe(getViewLifecycleOwner(), result -> {

            if(result.isSuccessful()){
                this.eventList.clear();
                this.eventList.addAll(((Result.EventSuccess) result).getData().getEventList());
                pointAnnotationManager.deleteAll();
                for(int i = 0; i<eventList.size(); i++){
                    Event event = eventList.get(i);
                    if(selfLocation != null){
                        double eventDistance = round(100*(Math.sqrt(Math.pow(selfLocation.latitude() - event.getLocation().getLatitude(),2)
                                + Math.pow(selfLocation.longitude() - event.getLocation().getLongitude(),2))),1);
                        event.setDistance(eventDistance);
                    }

                    eventViewModel.getWeather(event.getLocation().getLatitudeToString(), event.getLocation().getLongitudeToString()).observe(getViewLifecycleOwner(), weatherResult -> {
                        if(weatherResult.isSuccessful()){
                            weatherAPIdata = ((Result.WeatherSuccess) weatherResult).getData().getWeather();
                            String[] dateArray = weatherAPIdata.getHour();
                            double [] temperatureArray = weatherAPIdata.getTemperature();

                            boolean equals = false;
                            int indexDate = -1;
                            for(int j = 0;j < dateArray.length && !equals ; j+=96){
                                if(event.getDate().equals(dateArray[j].substring(0, 10))) {
                                    indexDate = j;
                                    equals = true;
                                }
                            }
                            int indexHour = Integer.parseInt(event.getTime().substring(0,2))*4;
                            int indexMinute = Integer.parseInt(event.getTime().substring(3,5))/15;
                            if (indexDate != -1){
                                event.setWeatherTemperature(temperatureArray[indexDate+indexHour+indexMinute]);
                                event.setWeatherCode(weatherAPIdata.getWeather_code(indexDate+indexHour+indexMinute));
                            } else {
                                event.setWeatherCode(-1);
                            }

                            pointAnnotationManager.create(new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER)
                                    .withPoint(Point.fromLngLat(event.getLocation().getLongitude(), event.getLocation().getLatitude()))
                                    .withIconImage(bitmap));
                        } else {
                            ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
                            Snackbar.make(view, errorMessagesUtil.getWeatherErrorMessage(((Result.WeatherError) weatherResult).getMessage()), Snackbar.LENGTH_LONG).show();
                        }
                    });
                    eventList.set(i, event);

                }
            } else {
                ErrorMessagesUtil errorMessagesUtil =
                        new ErrorMessagesUtil(requireActivity().getApplication());
                Snackbar.make(view, errorMessagesUtil.
                                getEventErrorMessage(VIEW_MODEL_ERROR),
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        GesturesUtils.addOnMapClickListener(mapView.getMapboxMap(), point -> {
                    eventItem.setVisibility(View.GONE);
            return false;
        });

        eventItem = view.findViewById(R.id.fragmentDiscoverMap_cardView_eventItem);

        pointAnnotationManager.addClickListener(annotation -> {

            boolean find = false;
            for(int i = 0; i<eventList.size() && !find; i++){
                if(eventList.get(i).getLocation().getLongitude() == annotation.getPoint().longitude() && eventList.get(i).getLocation().getLatitude() == annotation.getPoint().latitude()){
                    event = eventList.get(i);
                    find = true;
                }
            }
            //event = eventList.get((int)annotation.getId());
            eventItem.setVisibility(View.VISIBLE);
            fragmentDiscoverMapBinding.eventListItemTextViewEventTitle.setText(event.getTitle());
            fragmentDiscoverMapBinding.eventListItemTextViewDate.setText(event.getDate());
            fragmentDiscoverMapBinding.eventListItemTextViewTime.setText(event.getTime());
            fragmentDiscoverMapBinding.eventListItemTextViewPlace.setText(event.getLocation().getName());
            Log.d("code", event.getWeatherCode()+"");
            if(event.getWeatherCode() != -1){
                setWeatherIcon(fragmentDiscoverMapBinding.eventListItemImageViewWeather, event.getWeatherCode());
            }
            fragmentDiscoverMapBinding.eventListItemTextViewDistance.setText(event.getDistance()+" km");
            fragmentDiscoverMapBinding.eventListItemTextViewPeopleNumber.setText(event.getPeopleNumberString());
            fragmentDiscoverMapBinding.eventListItemImageViewBackground.setBackgroundColor(Color.parseColor(event.getColor().getHex()));
            fragmentDiscoverMapBinding.eventListItemButtonJoinButton.setBackgroundColor(Color.parseColor(event.getColor().getHex()));

            eventItem.setOnClickListener(v -> {
                DiscoverFragmentDirections.ActionFragmentDiscoverToFragmentDiscoverSingleEvent action = DiscoverFragmentDirections.actionFragmentDiscoverToFragmentDiscoverSingleEvent(event);
                Navigation.findNavController(view).navigate(action);

            });

            annotation.getPoint().latitude();
            annotation.getPoint().longitude();

            return true;
            });
        }

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
            updateCamera(eventCoordinates);
        }
    }

    private void updateCamera(Point point) {
        MapAnimationOptions animationOptions = new MapAnimationOptions.Builder().duration(1000L).build();
        CameraOptions cameraOptions = new CameraOptions.Builder().center(point).zoom(16.0).bearing(0.0).pitch(0.0)
                .padding(new EdgeInsets(1000.0, 0.0, 0.0, 0.0)).build();

        getCamera(mapView).easeTo(cameraOptions, animationOptions);
    }

    public void setWeatherIcon(ImageView weatherIcon, int code){
        if (code == 0){
            weatherIcon.setBackgroundResource(R.drawable.drawable_sun);
        } else if (code >= 1 && code <= 3){
            weatherIcon.setBackgroundResource(R.drawable.drawable_partlycloudy);
        } else if (code == 45 || code == 48){
            weatherIcon.setBackgroundResource(R.drawable.drawable_fog);
        } else if (code == 51 || code == 53 || code == 55 || code == 56 || code == 57) {
            weatherIcon.setBackgroundResource(R.drawable.drawable_drizzle);
        } else if (code == 61 || code == 63 || code == 65 || code == 66 || code == 67 || code == 80 || code == 81 || code == 82){
            weatherIcon.setBackgroundResource(R.drawable.drawable_rain);
        } else if (code == 71 || code == 73 || code == 75 || code == 77){
            weatherIcon.setBackgroundResource(R.drawable.drawable_snowlight);
        } else if (code == 85 || code == 86){
            weatherIcon.setBackgroundResource(R.drawable.drawable_snow);
        } else if (code == 95 || code == 96 || code == 99){
            weatherIcon.setBackgroundResource(R.drawable.drawable_thunderstorm);
        }
    }
}
