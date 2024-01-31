package it.unimib.enjoyn.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.ViewPagerAdapter;
import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.repository.IEventRepository;
import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.MapRepository;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.ui.viewmodels.EventViewModelFactory;
import it.unimib.enjoyn.util.ServiceLocator;


/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    EventViewModel eventViewModel;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverMapFragment newInstance() {
        return new DiscoverMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //creazione dell'eventViewModel si può svolgere anche in DiscoverMapFragment
        IEventRepository eventRepositoryWithLiveData = ServiceLocator.getInstance().getEventRepository(
                requireActivity().getApplication());
        IWeatherRepository weatherRepository = ServiceLocator.getInstance().getWeatherRepository();

        MapRepository mapRepository = ServiceLocator.getInstance().getMapRepository();

        eventViewModel = new ViewModelProvider(
                requireActivity(),
                new EventViewModelFactory(eventRepositoryWithLiveData, weatherRepository, mapRepository)).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.discoverFragment_tabLayout);
        viewPager2 = view.findViewById(R.id.discoverFragment_viewPager);
        if(this.getActivity() != null)
            viewPagerAdapter = new ViewPagerAdapter(this.getActivity());

        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setUserInputEnabled(false);
        //List<Event> prova = new ArrayList<>();
        //viewPagerAdapter.setData(prova);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.menu_toolbar, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menuToolbar_favoritesButton){
                    startActivityBasedOnCondition(R.id.action_fragmentDiscover_to_favoritesFragment, false);
                }
                return false;
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(tabLayout.getTabAt(position) != null)
                    tabLayout.getTabAt(position).select();
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