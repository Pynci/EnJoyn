package it.unimib.enjoyn.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.common.Cancelable;
import com.mapbox.geojson.Point;

import com.mapbox.maps.AnnotatedFeature;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.ViewAnnotationOptions;
import com.mapbox.maps.plugin.PuckBearing;
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin;
import com.mapbox.maps.plugin.animation.CameraAnimationsUtils;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;

import com.mapbox.maps.viewannotation.ViewAnnotationManager;

import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.databinding.FragmentNewEventBinding;
import it.unimib.enjoyn.databinding.FragmentNewEventMapBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link newEventMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newEventMap extends Fragment implements  PermissionsListener {
    private FragmentNewEventMapBinding fragmentNewEventMapBinding;
    private FragmentNewEventBinding fragmentNewEventBinding;
    MapView mapView;

    MapboxMap mapboxMap;

    Point point;
    private PermissionsManager permissionsManager;



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
        mapView.getMapboxMap().loadStyle(Style.MAPBOX_STREETS);
        //TODO con current position per resettare il posizionamento (aggiungere bottone)
        //TODO con spostamento a ricerca

        if (false)
            flyToCameraPosition();
        prova(com.mapbox.geojson.Point.fromLngLat(8.191926, 45.464098));

        addOnMapClickListener(new OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull Point point) {
                point = point;
                return false;
            }
        });


    }

    private void flyToCameraPosition() {
        Point cameraCenterCoordinates = com.mapbox.geojson.Point.fromLngLat(8.191926, 45.464098);
        final CameraAnimationsPlugin camera = CameraAnimationsUtils.getCamera(mapView);
        final Cancelable cancelable = camera.easeTo(
                new CameraOptions.Builder()
                        .center(cameraCenterCoordinates)
                        .bearing(0.0)
                        .pitch(0.0)
                        .zoom(13.0)
                        .build(),
                new MapAnimationOptions.Builder().duration(1500).build(), null);
        CameraOptions cameraOptions = new CameraOptions.Builder()
                .center(cameraCenterCoordinates)
                .bearing(130.0)
                .pitch(75.0)
                .zoom(13.0)
                .build();


        new MapAnimationOptions.Builder().duration(1500).build();

    }


    private void prova(Point point) {
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
    }

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



