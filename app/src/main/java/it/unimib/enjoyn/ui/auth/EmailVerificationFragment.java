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
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.viewmodels.UserViewModelFactory;
import it.unimib.enjoyn.util.ServiceLocator;
import it.unimib.enjoyn.util.SnackbarBuilder;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;

public class EmailVerificationFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_email_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceBundle) {
        super.onViewCreated(view, savedInstanceBundle);

        Button buttonForNewEmail = view.findViewById(R.id.fragmentConfirmEmailMessage_button_newEmail);
        Button buttonToLogin = view.findViewById(R.id.fragmentConfirmEmailMessage_button_buttonToLogin);
        Button buttonRefresh = view.findViewById(R.id.fragmentConfirmEmailMessage_button_refresh);
        ProgressBar progressBar = view.findViewById(R.id.fragmentConfirmEmailMessage_progressBar);

        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        emailVerificationSendingObserver = result -> {
            if(result.isSuccessful()){
                String text = "È stata inviata una nuova mail di conferma";
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildOkSnackbar(text, view, getContext(), currentTheme);
                snackbar.show();
            }
            else{
                String text = "Si è verificato un errore nell'invio della mail di conferma";
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(text, view, getContext(), currentTheme);
                snackbar.show();
            }
        };

        emailVerificationStatusObserver = result -> {
            if(result.isSuccessful()){
                User currentUser = ((Result.UserSuccess) result).getData();
                if(currentUser != null){
                    if(currentUser.getEmailVerified()){
                        navigateTo(R.id.action_emailVerificationFragment_to_profileConfigurationFragment, false);
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
                String text = "Si è verificato un errore durante il logout";
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(text, view, getContext(), currentTheme);
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
}