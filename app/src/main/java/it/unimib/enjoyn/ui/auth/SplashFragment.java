package it.unimib.enjoyn.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.UserViewModel;
import it.unimib.enjoyn.ui.UserViewModelFactory;
import it.unimib.enjoyn.util.ServiceLocator;

public class SplashFragment extends Fragment {

    private UserViewModel userViewModel;

    public SplashFragment() {
        // Required empty public constructor
    }

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel.getCurrentUser().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                redirect(((Result.UserSuccess) result).getData());
            }
        });
    }


    private void redirect(User currentUser){
        if(currentUser.getEmailVerified()){
            if(currentUser.getProfileConfigured()){
                Navigation
                        .findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_splashFragment_to_mainButtonMenuActivity);
            }
            else{
                Navigation
                        .findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_splashFragment_to_propicDescriptionConfigurationFragment);
            }
        }
        else{
            Navigation
                    .findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_splashFragment_to_confirmEmailMessageFragment);
        }
    }
}