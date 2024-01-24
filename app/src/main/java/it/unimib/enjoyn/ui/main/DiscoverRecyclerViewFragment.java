package it.unimib.enjoyn.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.EventReclyclerViewAdapter;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;


public class DiscoverRecyclerViewFragment extends Fragment {


    private EventViewModel eventViewModel;
    private List<Event> eventList;
    private EventReclyclerViewAdapter eventsRecyclerViewAdapter;


    public DiscoverRecyclerViewFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Discover.
     */
    // TODO: Rename and change types and number of parameters
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


        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.menu_toolbar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menuToolbar_favoritesButton){
                    startActivityBasedOnCondition(R.id.action_discover_to_favoritesFragment, false);
                }
                return false;
            }
        });

        RecyclerView recyclerViewDiscoverEvents = view.findViewById(R.id.discoverRecyclerView_recyclerview_event);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);


        eventsRecyclerViewAdapter = new EventReclyclerViewAdapter(eventList, requireActivity().getApplication(),
                new EventReclyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        DiscoverFragmentDirections.ActionDiscoverToDiscoverSingleEvent action =
                               DiscoverFragmentDirections.actionDiscoverToDiscoverSingleEvent(event);
                        //Navigation.findNavController(view).navigate(R.id.action_discover_to_discoverSingleEvent);
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onJoinButtonPressed(int position) {
                        eventList.get(position).setTODO(!eventList.get(position).isTODO());

                        if(eventList.get(position).isTODO()) {
                            eventList.get(position).incrementPeopleNumber();
                        }
                        else{
                            eventList.get(position).decrementPeopleNumber();
                        }
                        eventViewModel.updateEvent(eventList.get(position));

                    }
                });
        recyclerViewDiscoverEvents.setLayoutManager(layoutManager);
        recyclerViewDiscoverEvents.setAdapter(eventsRecyclerViewAdapter);

        eventViewModel.getEvent(Long.parseLong("0")).observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccessful()) {
                        int initialSize = this.eventList.size();
                        this.eventList.clear();
                        this.eventList.addAll(((Result.EventSuccess) result).getData().getEventList());
                        eventsRecyclerViewAdapter.notifyItemRangeInserted(initialSize, this.eventList.size());
                        eventsRecyclerViewAdapter.notifyDataSetChanged();

                    } else {
                        /*
                        ErrorMessagesUtil errorMessagesUtil =
                                new ErrorMessagesUtil(requireActivity().getApplication());
                        Snackbar.make(view, errorMessagesUtil.
                                        getErrorMessage(((Result.Error) result).getMessage()),
                                Snackbar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        */
                    }
                });
    }

    private void startActivityBasedOnCondition(int destination, boolean finishActivity) {
        Navigation.findNavController(requireView()).navigate(destination);

        //da utilizzare solo se si passa ad un'altra activity
        if (finishActivity){
            requireActivity().finish();
        }
    }


}