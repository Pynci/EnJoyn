package it.unimib.enjoyn.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;

public class DiscoverMapFragment extends Fragment {

    EventViewModel eventViewModel;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover_map, container, false);
    }
}