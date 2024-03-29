package it.unimib.enjoyn.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.tabs.TabLayout;



import it.unimib.enjoyn.adapter.ViewPagerAdapter;
import it.unimib.enjoyn.databinding.FragmentDiscoverBinding;
import it.unimib.enjoyn.repository.IEventRepository;
import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.MapRepository;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.viewmodels.EventViewModel;
import it.unimib.enjoyn.ui.viewmodels.EventViewModelFactory;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModelFactory;
import it.unimib.enjoyn.util.ServiceLocator;



public class DiscoverFragment extends Fragment {

    private FragmentDiscoverBinding fragmentDiscoverBinding;
    private EventViewModel eventViewModel;
    private UserViewModel userViewModel;
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
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());

        eventViewModel = new ViewModelProvider(
                requireActivity(),
                new EventViewModelFactory(eventRepositoryWithLiveData, weatherRepository, mapRepository)).get(EventViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentDiscoverBinding = FragmentDiscoverBinding.inflate(inflater, container, false);
        return fragmentDiscoverBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = fragmentDiscoverBinding.discoverFragmentTabLayout;
        viewPager2 = fragmentDiscoverBinding.discoverFragmentViewPager;
        if(this.getActivity() != null)
            viewPagerAdapter = new ViewPagerAdapter(this.getActivity());

        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setUserInputEnabled(false);

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



}