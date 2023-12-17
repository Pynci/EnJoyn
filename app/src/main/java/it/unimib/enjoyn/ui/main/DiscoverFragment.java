package it.unimib.enjoyn.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.EventReclyclerViewAdapter;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.repository.EventMockRepository;
import it.unimib.enjoyn.repository.IEventRepository;
import it.unimib.enjoyn.util.JSONParserUtil;
import it.unimib.enjoyn.util.ResponseCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment implements ResponseCallback {

    private IEventRepository iEventRepository;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button EventButton;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Discover.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
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

        iEventRepository = new EventMockRepository(requireActivity().getApplication(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Use getViewLifecycleOwner() to avoid that the listener
            // associated with a menu icon is called twice
             getViewLifecycleOwner();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);

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

        RecyclerView recyclerViewDiscoverEvents = view.findViewById(R.id.recyclerview_discover_event);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false);

        List<Event> eventList = getEventListWithGSon();

        //List<Event> eventList = new ArrayList<Event>() ;
        //eventList.add(new Event(5464, "patate al forno", "ciao come stai, mangio patate", "14/02/2023", "12.00", false, "casa di fra", "casa di fra", new Category("cibo"), 6, 2.6));


        EventReclyclerViewAdapter eventsRecyclerViewAdapter = new EventReclyclerViewAdapter(eventList,
                new EventReclyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onEventItemClick(Event event) {
                        startActivityBasedOnCondition(MainButtonMenuActivity.class, R.id.action_discover_to_discoverSingleEvent, false);
                    }

                    @Override
                    public void onJoinButtonPressed(int position) {
                        eventList.get(position).setTODO(!eventList.get(position).isTODO());
                        iEventRepository.updateTodo(eventList.get(position));

                    }
                });
        recyclerViewDiscoverEvents.setLayoutManager(layoutManager);
        recyclerViewDiscoverEvents.setAdapter(eventsRecyclerViewAdapter);
    }

    private void startActivityBasedOnCondition(Class<?> destinationActivity, int destination, boolean finishActivity) {
        if (true) {
            Navigation.findNavController(requireView()).navigate(destination);
        } else {
            Intent intent = new Intent(requireContext(), destinationActivity);
            startActivity(intent);
        }
        //da utilizzare solo se si passa ad un'altra activity
        if (finishActivity){
            requireActivity().finish();
        }
    }

    private List<Event> getEventListWithGSon() {
        JSONParserUtil jsonParserUtil = new JSONParserUtil(requireActivity().getApplication());
        try {
            /**TODO
             * sistemare questa parte
             * */

            Context context = requireActivity().getApplication().getApplicationContext();
            InputStream inputStream = context.getAssets().open("prova.json"); //apro file
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //estraggo json

            return jsonParserUtil.parseJSONEventFileWithGSon("prova.json").getEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onSuccess(List<Event> newsList, long lastUpdate) {

    }

    @Override
    public void onFailure(String errorMessage) {

    }

    @Override
    public void onEventFavoriteStatusChanged(Event event) {

    }

    @Override
    public void onEventTodoStatusChanged(Event event) {

    }
}