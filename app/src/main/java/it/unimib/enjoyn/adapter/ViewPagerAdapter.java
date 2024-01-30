package it.unimib.enjoyn.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.gson.Gson;

import java.util.List;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.ui.main.DiscoverMapFragment;
import it.unimib.enjoyn.ui.main.DiscoverRecyclerViewFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<Event> eventList;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setData(List<Event> newEventList)
    {
        eventList = newEventList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        Gson gson = new Gson();

        bundle.putString("eventList", gson.toJson(eventList));
        if (position == 1) {
            Fragment recyclerViewFragment = new DiscoverRecyclerViewFragment();

            recyclerViewFragment.setArguments(bundle);
            return recyclerViewFragment;
        } else {
            Fragment mapFragment = new DiscoverMapFragment();

            mapFragment.setArguments(bundle);
            return mapFragment;
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
