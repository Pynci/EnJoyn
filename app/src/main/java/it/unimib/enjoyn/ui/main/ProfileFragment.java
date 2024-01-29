package it.unimib.enjoyn.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.unimib.enjoyn.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getViewLifecycleOwner();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
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

        Button modify = view.findViewById(R.id.fragmentProfile_textButton_editProfile);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragmentDirections.ActionProfileFragmentToProfileConfigurationFragment2 action = ProfileFragmentDirections.actionProfileFragmentToProfileConfigurationFragment2();
                startActivityBasedOnCondition(R.id.action_profileFragment_to_profileConfigurationFragment2, false);
            }
        });

        /*
        //prova swicth di un cointener

        ViewSwitcher viewSwitcher = (ViewSwitcher) view.findViewById(R.id.viewSwitcher1);
        View myFirstView = view.findViewById(R.id.view1);
        View mySecondView = view.findViewById(R.id.view2);
        Button button1 = (Button) view.findViewById(R.id.fragmentProfile_textButton_editProfile);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (viewSwitcher.getCurrentView() != myFirstView) {
                    viewSwitcher.showPrevious();
                } else if (viewSwitcher.getCurrentView() != mySecondView) {
                    viewSwitcher.showNext();
                }
            }
        });

         */
    }

    private void startActivityBasedOnCondition(int destination, boolean finishActivity) {
        Navigation.findNavController(requireView()).navigate(destination);

        //da utilizzare solo se si passa ad un'altra activity
        if (finishActivity){
            requireActivity().finish();
        }
    }
}