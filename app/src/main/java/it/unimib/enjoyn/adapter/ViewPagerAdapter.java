package it.unimib.enjoyn.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import it.unimib.enjoyn.ui.main.DiscoverMapFragment;
import it.unimib.enjoyn.ui.main.DiscoverRecyclerViewFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new DiscoverMapFragment();
            case 1: return  new DiscoverRecyclerViewFragment();
            default: return new DiscoverMapFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
