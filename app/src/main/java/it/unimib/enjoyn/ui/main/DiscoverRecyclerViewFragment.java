package it.unimib.enjoyn.ui.main;

import static it.unimib.enjoyn.util.Constants.VIEW_MODEL_ERROR;

import android.content.res.Configuration;
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
import it.unimib.enjoyn.databinding.FragmentDiscoverRecyclerViewBinding;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.ui.viewmodels.InterestViewModelFactory;
import it.unimib.enjoyn.ui.viewmodels.InterestsViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;
import it.unimib.enjoyn.util.ErrorMessagesUtil;
import it.unimib.enjoyn.util.ServiceLocator;
import it.unimib.enjoyn.util.SnackbarBuilder;


public class DiscoverRecyclerViewFragment extends Fragment {


    private FragmentDiscoverRecyclerViewBinding fragmentDiscoverRecyclerViewBinding;
    private EventViewModel eventViewModel;
    private InterestsViewModel interestsViewModel;
    private UserViewModel userViewModel;
    private List<Event> eventList;
    private List<Category> categoryList;
    private List<Event> interestedEventList;
    private User user;
    private EventReclyclerViewAdapter eventsRecyclerViewAdapter;


    public DiscoverRecyclerViewFragment() {
    }

    public static DiscoverRecyclerViewFragment newInstance() {

        return new DiscoverRecyclerViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        interestsViewModel = new ViewModelProvider(requireActivity(), new InterestViewModelFactory(requireActivity().getApplication())).get(InterestsViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        eventList = new ArrayList<>();
        interestedEventList = new ArrayList<>();
        categoryList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Use getViewLifecycleOwner() to avoid that the listener
        // associated with a menu icon is called twice
        getViewLifecycleOwner();
        fragmentDiscoverRecyclerViewBinding = FragmentDiscoverRecyclerViewBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return fragmentDiscoverRecyclerViewBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        RecyclerView recyclerViewDiscoverEvents = fragmentDiscoverRecyclerViewBinding.discoverRecyclerViewRecyclerviewEvent;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);


        eventsRecyclerViewAdapter = new EventReclyclerViewAdapter(interestedEventList, getContext(),
                new EventReclyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        DiscoverFragmentDirections.ActionFragmentDiscoverToFragmentDiscoverSingleEvent action =
                               DiscoverFragmentDirections.actionFragmentDiscoverToFragmentDiscoverSingleEvent(event);
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onJoinButtonPressed(int position) {
                        if(eventList.get(position).isTodo()){
                            Snackbar snackbar;
                            snackbar = SnackbarBuilder.buildOkSnackbar(R.string.eventAddToDo, view, getContext(), currentTheme);
                            snackbar.show();
                        } else {
                            Snackbar snackbar;
                            snackbar = SnackbarBuilder.buildOkSnackbar(R.string.eventRemoveToDo, view, getContext(), currentTheme);
                            snackbar.show();
                        }
                    }


                } );
        recyclerViewDiscoverEvents.setLayoutManager(layoutManager);
        recyclerViewDiscoverEvents.setAdapter(eventsRecyclerViewAdapter);

        interestsViewModel.getInterests().observe(getViewLifecycleOwner(),
                result -> {
                    if (result.isSuccessful()){
                        this.categoryList.clear();
                        this.categoryList.addAll(((Result.CategorySuccess) result).getCategoryList());

                        eventViewModel.getAllEvents().observe(getViewLifecycleOwner(),
                                resultEvent -> {
                                    if (result.isSuccessful()) {
                                        this.eventList.clear();
                                        this.eventList.addAll(((Result.EventSuccess) resultEvent).getData().getEventList());
                                        Boolean find;
                                        int initialSize = this.interestedEventList.size();
                                        this.interestedEventList.clear();
                                        for (Event event: eventList) {
                                            find = false;
                                            for (int i = 0; i<categoryList.size() && !find; i++){
                                                if (categoryList.get(i).equals(event.getCategory())){
                                                    interestedEventList.add(event);
                                                    find = true;
                                                }
                                            }
                                        }

                                        if(interestedEventList.isEmpty()){
                                            fragmentDiscoverRecyclerViewBinding.discoverRecyclerViewFragmentTextViewNoEvents.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            fragmentDiscoverRecyclerViewBinding.discoverRecyclerViewFragmentTextViewNoEvents.setVisibility(View.GONE);
                                        }

                                        eventsRecyclerViewAdapter.notifyItemRangeInserted(initialSize, this.interestedEventList.size());
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
        });





    }



}