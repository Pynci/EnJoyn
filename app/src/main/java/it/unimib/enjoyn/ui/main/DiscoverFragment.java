package it.unimib.enjoyn.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.tabs.TabLayout;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.adapter.ViewPagerAdapter;
import it.unimib.enjoyn.repository.IEventRepositoryWithLiveData;
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
        IEventRepositoryWithLiveData eventRepositoryWithLiveData = ServiceLocator.getInstance().getEventRepository(
                requireActivity().getApplication());
        IWeatherRepository weatherRepository = ServiceLocator.getInstance().getWeatherRepository(requireActivity().getApplication());

        MapRepository mapRepository = ServiceLocator.getInstance().getMapRepository(requireActivity().getApplication());

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
        viewPagerAdapter = new ViewPagerAdapter(this.getActivity());
        viewPager2.setAdapter(viewPagerAdapter);
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
                tabLayout.getTabAt(position).select();
            }
        });

    }

}