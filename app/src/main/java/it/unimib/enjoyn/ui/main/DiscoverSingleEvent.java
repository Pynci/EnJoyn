package it.unimib.enjoyn.ui.main;

import android.graphics.Color;
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
import android.widget.ImageView;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.databinding.FragmentDiscoverSingleEventBinding;
import it.unimib.enjoyn.model.Category;
import it.unimib.enjoyn.model.Event;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverSingleEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverSingleEvent extends Fragment {

    private FragmentDiscoverSingleEventBinding fragmentDiscoverSingleEventBinding;

    public DiscoverSingleEvent() {
        // Required empty public constructor
    }
    public static DiscoverSingleEvent newInstance() {
        return new DiscoverSingleEvent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentDiscoverSingleEventBinding = FragmentDiscoverSingleEventBinding.inflate(inflater, container, false);
        return fragmentDiscoverSingleEventBinding.getRoot();
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
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewPlace.setText(event.getLocation().getName());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewTitle.setText(event.getTitle());
        fragmentDiscoverSingleEventBinding.discoverSingleEventTextViewDistance.setText(event.getDistanceString());
        setWeatherIcon(fragmentDiscoverSingleEventBinding.fragmentDiscoverSingleEventImageViewWeather, event.getWeatherCode());
        fragmentDiscoverSingleEventBinding.fragmentDiscoverSingleEventTextViewTemperature.setText(event.getWeatherTemperature()+"°");
        fragmentDiscoverSingleEventBinding.fragmentDiscoverSingleEventChipCategory.setText(event.getCategory().getNome());

        fragmentDiscoverSingleEventBinding.discoverSingleEventButtonJoin.setBackgroundColor(Color.parseColor(event.getColor().getHex()));
        fragmentDiscoverSingleEventBinding.eventListItemImageViewBackground.setBackgroundColor(Color.parseColor(event.getColor().getHex()));
        setCategoryImage(fragmentDiscoverSingleEventBinding.fragmentDiscoverSingleEventImageViewCategoryVector, event.getCategory());

    }

    public void setWeatherIcon(ImageView weatherIcon, int code){
        if (code == 0){
            weatherIcon.setBackgroundResource(R.drawable.drawable_sun);
        } else if (code >= 1 && code <= 3){
            weatherIcon.setBackgroundResource(R.drawable.drawable_partlycloudy);
        } else if (code == 45 || code == 48){
            weatherIcon.setBackgroundResource(R.drawable.drawable_fog);
        } else if (code == 51 || code == 53 || code == 55 || code == 56 || code == 57) {
            weatherIcon.setBackgroundResource(R.drawable.drawable_drizzle);
        } else if (code == 61 || code == 63 || code == 65 || code == 66 || code == 67 || code == 80 || code == 81 || code == 82){
            weatherIcon.setBackgroundResource(R.drawable.drawable_rain);
        } else if (code == 71 || code == 73 || code == 75 || code == 77){
            weatherIcon.setBackgroundResource(R.drawable.drawable_snowlight);
        } else if (code == 85 || code == 86){
            weatherIcon.setBackgroundResource(R.drawable.drawable_snow);
        } else if (code == 95 || code == 96 || code == 99){
            weatherIcon.setBackgroundResource(R.drawable.drawable_thunderstorm);
        }
    }

    public void setCategoryImage(ImageView imageView, Category category){
        switch (category.getNome()){
            case "Passeggiata":
                imageView.setBackgroundResource(R.drawable.passeggiata);
                break;
            case "Viaggi":
                imageView.setBackgroundResource(R.drawable.viaggi);
                break;
            case "Pranzo":
                imageView.setBackgroundResource(R.drawable.pranzo);
                break;
            case "Videogiochi":
                imageView.setBackgroundResource(R.drawable.videogiochi);
                break;
            case "Shopping":
                imageView.setBackgroundResource(R.drawable.shopping);
                break;
            case "Cinema":
                imageView.setBackgroundResource(R.drawable.cinema);
                break;
            case "Sport":
                imageView.setBackgroundResource(R.drawable.sport);
                break;
        }

    }
}