package it.unimib.enjoyn.ui.auth.registration;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.UserViewModel;

public class ConfirmEmailMessageFragment extends Fragment {

    private UserViewModel userViewModel;
    private Observer<Result> emailVerificationSendingObserver;

    public ConfirmEmailMessageFragment() {
    }

    public static ConfirmEmailMessageFragment newInstance() {
        return new ConfirmEmailMessageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        // disabilita il tasto back affinché l'utente non possa tornare indietro
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // logica personalizzata per il tasto back (in questo caso non deve fare niente)
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_email_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceBundle) {
        super.onViewCreated(view, savedInstanceBundle);

        Button buttonForNewEmail = view.findViewById(R.id.fragmentConfirmEmailMessage_button_newEmail);
        Button buttonToLogin = view.findViewById(R.id.fragmentConfirmEmailMessage_button_buttonToLogin);

        emailVerificationSendingObserver = result -> {
            if(result.isSuccessful()){
                Snackbar.make(view, "È stata inviata una nuova mail di conferma",
                                Snackbar.LENGTH_SHORT)
                        .show();
            }
            else{
                Snackbar.make(view, "Si è verificato un errore nell'invio della mail di conferma",
                                Snackbar.LENGTH_SHORT)
                        .show();
            }
        };

        buttonForNewEmail.setOnClickListener(v -> userViewModel.sendEmailVerification().observe(getViewLifecycleOwner(),
                emailVerificationSendingObserver));

        buttonToLogin.setOnClickListener(v -> {
            userViewModel.signOut();
            Navigation
                    .findNavController(v)
                    .navigate(R.id.action_confirmEmailMessageFragment_to_loginActivity);
        });
    }
}