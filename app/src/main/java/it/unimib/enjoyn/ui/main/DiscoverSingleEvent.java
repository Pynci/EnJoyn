package it.unimib.enjoyn.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.databinding.FragmentDiscoverSingleEventBinding;
import it.unimib.enjoyn.databinding.FragmentNewEventBinding;
import it.unimib.enjoyn.model.Event;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverSingleEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverSingleEvent extends Fragment {

    private FragmentDiscoverSingleEventBinding fragmentDiscoverSingleEventBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscoverSingleEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverSingleEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverSingleEvent newInstance(String param1, String param2) {
        DiscoverSingleEvent fragment = new DiscoverSingleEvent();
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
        fragmentDiscoverSingleEventBinding = FragmentDiscoverSingleEventBinding.inflate(inflater, container, false);
        return fragmentDiscoverSingleEventBinding.getRoot();
     //   return inflater.inflate(R.layout.fragment_discover_single_event, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == android.R.id.home) {
                    Navigation.findNavController(requireView()).navigateUp();
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        Event event = DiscoverSingleEventArgs.fromBundle(getArguments()).getEvent();

        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewNumberOfParticipants.setText(event.getPeopleNumberString());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewDate.setText(event.getDate());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewDescription.setText(event.getDescription());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewHour.setText(event.getTime());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewPlace.setText(event.getPlace());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewTitle.setText(event.getTitle());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewDistance.setText(event.getDistanceString());
        boolean isTodo = event.isTODO();
        if(isTodo){
            fragmentDiscoverSingleEventBinding.discoverSingleEventButtonJoin.setText(R.string.remove);
        }else{
            fragmentDiscoverSingleEventBinding.discoverSingleEventButtonJoin.setText(R.string.Join);
        }
        //DiscoverSingleEventArgs.fromBundle(getArguments()).getEvent().setDistance(44.44);

    }
}