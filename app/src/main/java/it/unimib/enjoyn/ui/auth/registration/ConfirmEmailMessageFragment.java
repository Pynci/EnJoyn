package it.unimib.enjoyn.ui.auth.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.enjoyn.R;

public class ConfirmEmailMessageFragment extends Fragment {


    public ConfirmEmailMessageFragment() {
        // Required empty public constructor
    }

    public static ConfirmEmailMessageFragment newInstance() {
        return new ConfirmEmailMessageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        });

        buttonToLogin.setOnClickListener(v -> {


        });
    }
}