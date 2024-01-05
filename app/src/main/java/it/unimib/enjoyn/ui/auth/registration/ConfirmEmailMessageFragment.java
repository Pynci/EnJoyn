package it.unimib.enjoyn.ui.auth.registration;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.enjoyn.R;

public class ConfirmEmailMessageFragment extends Fragment {

    public static final String TAG = RegisterFragment.class.getSimpleName();

    public ConfirmEmailMessageFragment() {
        // Required empty public constructor
    }

    public static ConfirmEmailMessageFragment newInstance() {
        return new ConfirmEmailMessageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        buttonForNewEmail.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            firebaseUser.sendEmailVerification().addOnCompleteListener(task -> {

                if(task.isSuccessful()) {
                    Snackbar.make(view, "È stata inviata una nuova mail di conferma a " +
                                    firebaseUser.getEmail(), Snackbar.LENGTH_SHORT)
                            .show();
                }
                else{

                    Snackbar.make(view, "Si è verificato un errore nell'invio della mail di conferma." +
                                            "Riprovare tra qualche minuto",
                                    Snackbar.LENGTH_SHORT)
                            .show();
                }
            });

        });

        buttonToLogin.setOnClickListener(v -> {
            Navigation
                    .findNavController(v)
                    .navigate(R.id.action_confirmEmailMessageFragment2_to_loginActivity);
        });
    }
}