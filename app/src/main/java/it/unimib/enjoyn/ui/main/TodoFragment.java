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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.EventReclyclerViewAdapter;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.util.ErrorMessagesUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends Fragment {


    private EventViewModel eventViewModel;
   // private UserViewModel userViewModel;
    private List<Event> eventList;
    private List<Event> todoEventList;
    private User user;
    private EventReclyclerViewAdapter eventsRecyclerViewAdapter;

    public TodoFragment() {
        // Required empty public constructor
    }


    public static TodoFragment newInstance() {

        return new TodoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventList = new ArrayList<>();
        todoEventList = new ArrayList<>();

        //IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        //userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getViewLifecycleOwner();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        RecyclerView recyclerViewDiscoverEvents = view.findViewById(R.id.fragmentTODO_recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

         eventsRecyclerViewAdapter = new EventReclyclerViewAdapter(todoEventList, getContext(),
                new EventReclyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        TodoFragmentDirections.ActionFragmentTodoToFragmentDiscoverSingleEvent action =
                                TodoFragmentDirections.actionFragmentTodoToFragmentDiscoverSingleEvent(event);
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onJoinButtonPressed(int position) {
                        //eventList.get(position).setTODO(!eventList.get(position).isTODO());
                        //if(eventList.get(position).isTODO()) {
                            //eventList.get(position).incrementPeopleNumber();
                        //}
                        //else{
                            //eventList.get(position).decrementPeopleNumber();
                        //}
//                        eventViewModel.updateEvent(eventList.get(position));

 /*                       Event event = eventList.get(position);
                        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), result -> {
                            if(result.isSuccessful()){
                                user = ((Result.UserSuccess) result).getData();
                                if(event.isTodo()){
                                    eventViewModel.leaveEvent(event, user);
                                } else {
                                    eventViewModel.joinEvent(event, user);
                                }
                            }
                        });
                        eventsRecyclerViewAdapter.notifyDataSetChanged();*/
                    }
                });
        recyclerViewDiscoverEvents.setLayoutManager(layoutManager);
        recyclerViewDiscoverEvents.setAdapter(eventsRecyclerViewAdapter);

        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (result.isSuccessful()) {

                    this.eventList.clear();
                    this.eventList.addAll(((Result.EventSuccess) result).getData().getEventList());

                    int initialSize = this.todoEventList.size();
                    todoEventList.clear();
                    for (Event event: eventList){
                        if(event.isTodo()){
                            todoEventList.add(event);
                        }
                    }
                    eventsRecyclerViewAdapter.notifyItemRangeInserted(initialSize, this.todoEventList.size());
                    eventsRecyclerViewAdapter.notifyDataSetChanged();

                } else {
                    ErrorMessagesUtil errorMessagesUtil =
                            new ErrorMessagesUtil(requireActivity().getApplication());
                    Snackbar.make(view, errorMessagesUtil.getEventErrorMessage(((Result.Error)result).getMessage()),
                            Snackbar.LENGTH_SHORT).show();
                }
               // progressBar.setVisibility(View.GONE);
            }
        });


    }




    /*@Override
    public void onSuccess(List<Event> eventList, long lastUpdate) {
        if (eventList != null) {
            this.eventList.clear();
            this.eventList.addAll(eventList);
            requireActivity().runOnUiThread(() -> {
                eventsRecyclerViewAdapter.notifyDataSetChanged();
                //progressBar.setVisibility(View.GONE);
            });
        }
    }

    @Override
    public void onFailure(String errorMessage) {

    }

    @Override
    public void onEventFavoriteStatusChanged(Event event) {

    }

    @Override
    public void onEventTodoStatusChanged(Event event) {
        eventList.remove(event);
        if (event.isTODO()) {
            requireActivity().runOnUiThread(() -> eventsRecyclerViewAdapter.notifyDataSetChanged());
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.eventAddToDo),
                    Snackbar.LENGTH_LONG).show();
        } else {
            requireActivity().runOnUiThread(() -> eventsRecyclerViewAdapter.notifyDataSetChanged());
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.eventRemoveToDo),
                    Snackbar.LENGTH_LONG).show();
        }
    }*/
}