package it.unimib.enjoyn.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.enjoyn.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverMapFragment extends Fragment {

    EventViewModel eventViewModel;


    public DiscoverMapFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DiscoverMapFragment newInstance(String param1, String param2) {
        DiscoverMapFragment fragment = new DiscoverMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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