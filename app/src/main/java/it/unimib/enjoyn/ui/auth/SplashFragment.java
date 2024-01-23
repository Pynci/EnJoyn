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

        userViewModel.refreshSession().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                User currentUser = ((Result.UserSuccess) result).getData();
                if(currentUser != null){
                    if(!currentUser.getEmailVerified()){
                        navigateTo(R.id.action_splashFragment_to_confirmEmailMessageFragment, false);
                    }
                    if(!currentUser.getProfileConfigured()){
                        navigateTo(R.id.action_splashFragment_to_propicDescriptionConfigurationFragment, false);
                    }
                    if(!currentUser.getCategoriesSelectionDone()){
                        navigateTo(R.id.action_splashFragment_to_categoriesSelectionFragment, false);
                    }
                    navigateTo(R.id.action_splashFragment_to_mainButtonMenuActivity, true);
                }
                else{
                    navigateTo(R.id.action_splashFragment_to_loginFragment, false);
                }
            }
            else{
                navigateTo(R.id.action_splashFragment_to_loginFragment, false);
            }
        });
    }

    private void navigateTo(int destination, boolean finishActivity) {
        Navigation.findNavController(requireView()).navigate(destination);
        if (finishActivity) {
            requireActivity().finish();
        }
    }
}