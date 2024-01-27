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
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;
import it.unimib.enjoyn.ui.viewmodels.UserViewModelFactory;
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

        userViewModel.refreshSession().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                User currentUser = ((Result.UserSuccess) result).getData();
                if(currentUser != null){
                    if(!currentUser.getEmailVerified()){
                        navigateTo(R.id.action_splashFragment_to_confirmEmailMessageFragment, false);
                    }
                    else if(!currentUser.getProfileConfigured()){
                        navigateTo(R.id.action_splashFragment_to_propicDescriptionConfigurationFragment, false, false);
                    }
                    else if(!currentUser.getCategoriesSelectionDone()){
                        navigateTo(R.id.action_splashFragment_to_categoriesSelectionFragment, false);
                    }
                    else{
                        navigateTo(R.id.action_splashFragment_to_mainButtonMenuActivity, true);
                    }
                }
                else{
                    navigateTo(R.id.action_splashFragment_to_signinFragment, false);
                }
            }
            else{
                navigateTo(R.id.action_splashFragment_to_signinFragment, false);
            }
        });
    }

    private void navigateTo(int destination, boolean finishActivity) {
        Navigation.findNavController(requireView()).navigate(destination);
        if (finishActivity) {
            requireActivity().finish();
        }
    }

    private void navigateTo(int destination, boolean finishActivity, boolean fromProfileFragment) {

        Bundle bundle = new Bundle();
        bundle.putBoolean("fromProfileFragment", fromProfileFragment);
        Navigation.findNavController(requireView()).navigate(destination, bundle);
        if (finishActivity) {
            requireActivity().finish();
        }
    }
}