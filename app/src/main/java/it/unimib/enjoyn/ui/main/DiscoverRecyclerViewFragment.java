package it.unimib.enjoyn.ui.main;

import static it.unimib.enjoyn.util.Constants.VIEW_MODEL_ERROR;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.EventReclyclerViewAdapter;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.util.ErrorMessagesUtil;


public class DiscoverRecyclerViewFragment extends Fragment {


    private EventViewModel eventViewModel;
    private List<Event> eventList;
    private EventReclyclerViewAdapter eventsRecyclerViewAdapter;


    public DiscoverRecyclerViewFragment() {
    }

    public static DiscoverRecyclerViewFragment newInstance() {

        return new DiscoverRecyclerViewFragment();
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
        // Use getViewLifecycleOwner() to avoid that the listener
        // associated with a menu icon is called twice
        getViewLifecycleOwner();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover_recycler_view, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerViewDiscoverEvents = view.findViewById(R.id.discoverRecyclerView_recyclerview_event);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);


        eventsRecyclerViewAdapter = new EventReclyclerViewAdapter(eventList, requireActivity().getApplication(),
                new EventReclyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        DiscoverFragmentDirections.ActionFragmentDiscoverToFragmentDiscoverSingleEvent action =
                               DiscoverFragmentDirections.actionFragmentDiscoverToFragmentDiscoverSingleEvent(event);
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onJoinButtonPressed(int position) {


                     /* Event joinEvent = eventList.get(position);
                        joinEvent.setParticipants(joinEvent.getParticipants()+1);
                        eventViewModel.updateEvent(joinEvent);

                        eventList.get(position).setTODO(!eventList.get(position).isTODO());

                        if(eventList.get(position).isTODO()) {
                            eventList.get(position).incrementPeopleNumber();
                        }
                        else{
                            eventList.get(position).decrementPeopleNumber();
                        }
                        eventViewModel.updateEvent(eventList.get(position));

                         */

                    }
                });
        recyclerViewDiscoverEvents.setLayoutManager(layoutManager);
        recyclerViewDiscoverEvents.setAdapter(eventsRecyclerViewAdapter);

        eventViewModel.getEvent().observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccessful()) {
                        int initialSize = this.eventList.size();
                        this.eventList.clear();
                        this.eventList.addAll(((Result.EventSuccess) result).getData().getEventList());
                        eventsRecyclerViewAdapter.notifyItemRangeInserted(initialSize, this.eventList.size());
                        eventsRecyclerViewAdapter.notifyDataSetChanged();

                    } else {

                        ErrorMessagesUtil errorMessagesUtil =
                                new ErrorMessagesUtil(requireActivity().getApplication());
                        Snackbar.make(view, errorMessagesUtil.
                                        getEventErrorMessage(VIEW_MODEL_ERROR),
                                Snackbar.LENGTH_SHORT).show();

                    }
                });

    }



}