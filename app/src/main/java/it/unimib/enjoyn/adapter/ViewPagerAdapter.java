package it.unimib.enjoyn.adapter;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;



import java.util.List;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.ui.main.DiscoverMapFragment;
import it.unimib.enjoyn.ui.main.DiscoverRecyclerViewFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<Event> eventList;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 1) {
            Fragment recyclerViewFragment = new DiscoverRecyclerViewFragment();


            return recyclerViewFragment;
        } else {
            Fragment mapFragment = new DiscoverMapFragment();


            return mapFragment;
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
