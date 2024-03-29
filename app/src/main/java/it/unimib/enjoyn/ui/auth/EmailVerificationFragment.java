package it.unimib.enjoyn.ui.auth;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.databinding.FragmentEmailVerificationBinding;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.viewmodels.UserViewModelFactory;
import it.unimib.enjoyn.util.ErrorMessagesUtil;
import it.unimib.enjoyn.util.ServiceLocator;
import it.unimib.enjoyn.util.SnackbarBuilder;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;

public class EmailVerificationFragment extends Fragment {

    private FragmentEmailVerificationBinding fragmentEmailVerificationBinding;
    private UserViewModel userViewModel;
    private Observer<Result> emailVerificationSendingObserver;
    private Observer<Result> emailVerificationStatusObserver;
    private Observer<Result> signOutObserver;

    public EmailVerificationFragment() {
    }

    public static EmailVerificationFragment newInstance() {
        return new EmailVerificationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        // disabilita il tasto back affinché l'utente non possa tornare indietro
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentEmailVerificationBinding = FragmentEmailVerificationBinding.inflate(inflater, container, false);
        return fragmentEmailVerificationBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceBundle) {
        super.onViewCreated(view, savedInstanceBundle);

        Button buttonForNewEmail = fragmentEmailVerificationBinding.fragmentConfirmEmailMessageButtonNewEmail;
        Button buttonToLogin = fragmentEmailVerificationBinding.fragmentConfirmEmailMessageButtonButtonToLogin;
        Button buttonRefresh = fragmentEmailVerificationBinding.fragmentConfirmEmailMessageButtonRefresh;
        ProgressBar progressBar = fragmentEmailVerificationBinding.fragmentConfirmEmailMessageProgressBar;

        ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());
        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        emailVerificationSendingObserver = result -> {
            if(result.isSuccessful()){
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildOkSnackbar(R.string.new_email_sent, view, getContext(), currentTheme);
                snackbar.show();
            }
            else{
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(errorMessagesUtil.getUserErrorMessage(((Result.Error) result).getMessage()), view, getContext(), currentTheme);
                snackbar.show();
            }
        };

        emailVerificationStatusObserver = result -> {
            if(result.isSuccessful()){
                User currentUser = ((Result.UserSuccess) result).getData();
                if(currentUser != null){
                    if(currentUser.getEmailVerified()){
                        navigateTo(R.id.action_emailVerificationFragment_to_profileConfigurationFragment, false, false);
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        };

        signOutObserver = result -> {
            if(result.isSuccessful()){
                navigateTo(R.id.action_emailVerificationFragment_to_signinFragment, false);
            }
            else{
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(R.string.logout_error, view, getContext(), currentTheme);
                snackbar.show();
            }
        };

        buttonForNewEmail.setOnClickListener(v -> userViewModel.sendEmailVerification().observe(getViewLifecycleOwner(),
                emailVerificationSendingObserver));

        buttonToLogin.setOnClickListener(v -> userViewModel.signOut().observe(getViewLifecycleOwner(), signOutObserver));

        buttonRefresh.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            userViewModel.updateEmailVerificationStatus()
                    .observe(getViewLifecycleOwner(),
                            emailVerificationStatusObserver);
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