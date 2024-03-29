package it.unimib.enjoyn.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.databinding.FragmentDiscoverSingleEventBinding;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;
import it.unimib.enjoyn.util.ImageConverter;

public class DiscoverSingleEventFragment extends Fragment {

    private FragmentDiscoverSingleEventBinding fragmentDiscoverSingleEventBinding;
    private EventViewModel eventViewModel;
    private UserViewModel userViewModel;
    private Event event;
    private ImageConverter imageConverter;

    public DiscoverSingleEventFragment() {
        // Required empty public constructor
    }
    public static DiscoverSingleEventFragment newInstance() {
        return new DiscoverSingleEventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(EventViewModel.class);
        userViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentDiscoverSingleEventBinding = FragmentDiscoverSingleEventBinding.inflate(inflater, container, false);
        return fragmentDiscoverSingleEventBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageConverter = new ImageConverter();
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

        event = DiscoverSingleEventFragmentArgs.fromBundle(getArguments()).getEvent();
        setEventParameters();
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewDistance.setText(event.getDistanceString());
        imageConverter.setWeatherIcon(fragmentDiscoverSingleEventBinding.fragmentDiscoverSingleEventImageViewWeather, event.getWeatherCode());
        fragmentDiscoverSingleEventBinding.fragmentDiscoverSingleEventTextViewTemperature.setText(event.getWeatherTemperature()+"°");

        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewPlace.setOnClickListener(placeView -> {
          Uri gmmIntentUri = Uri.parse("google.navigation:q=" + event.getLocation().getLatitude() + "," + event.getLocation().getLongitude());
          Intent googleMapsIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
          googleMapsIntent.setPackage("com.google.android.apps.maps");
          startActivity(googleMapsIntent);
        });

        eventViewModel.refreshEvent(event).observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                event = ((Result.SingleEventSuccess) result).getEvent();
                setEventParameters();
            }
            else{
                Snackbar.make(view, ((Result.Error) result).getMessage() , Snackbar.LENGTH_SHORT).show();
            }
        });

        fragmentDiscoverSingleEventBinding.discoverSingleEventButtonJoin.setOnClickListener(listener -> {
            if(getContext() != null){
                userViewModel.getCurrentUser().observe((LifecycleOwner) getContext(), result -> {
                    if(result.isSuccessful()){
                        User user = ((Result.UserSuccess) result).getData();
                        if(event.isTodo()){
                            eventViewModel.leaveEvent(event, user).observe((LifecycleOwner) getContext(), result1 -> {
                                if(result1.isSuccessful()){
                                    fragmentDiscoverSingleEventBinding.discoverSingleEventButtonJoin.setText(R.string.Join);
                                }

                            });
                        } else {
                            eventViewModel.joinEvent(event, user).observe((LifecycleOwner) getContext(), result2 -> {
                                if(result2.isSuccessful()){
                                    fragmentDiscoverSingleEventBinding.discoverSingleEventButtonJoin.setText(R.string.remove);
                                }

                            });
                        }

                    }
                });
            }
        });

    }

    private void setEventParameters() {
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewNumberOfParticipants.setText(event.getPeopleNumberString());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewDate.setText(event.getDate());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewDescription.setText(event.getDescription());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewHour.setText(event.getTime());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewPlace.setText(event.getLocation().getName());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewTitle.setText(event.getTitle());
        fragmentDiscoverSingleEventBinding.fragmentDiscoverSingleEventChipCategory.setText(event.getCategory().getNome());
        fragmentDiscoverSingleEventBinding.discoverSingleEventButtonJoin.setBackgroundColor(Color.parseColor(event.getColor().getHex()));
        fragmentDiscoverSingleEventBinding.eventListItemImageViewBackground.setBackgroundColor(Color.parseColor(event.getColor().getHex()));
        if(event.isTodo()){
            fragmentDiscoverSingleEventBinding.discoverSingleEventButtonJoin.setText(R.string.remove);
        } else {
            fragmentDiscoverSingleEventBinding.discoverSingleEventButtonJoin.setText(R.string.Join);
        }
        imageConverter.setCategoryImage(fragmentDiscoverSingleEventBinding.fragmentDiscoverSingleEventImageViewCategoryVector, event.getCategory().getNome());

    }

}