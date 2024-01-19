package it.unimib.enjoyn.ui.auth;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.UserViewModel;

public class PasswordResetFragment extends Fragment {

    private Observer<Result> emailRecoverPasswordObserver;
    private UserViewModel userViewModel;

    public PasswordResetFragment() {
        // Required empty public constructor
    }

    public static PasswordResetFragment newInstance() {
        return new PasswordResetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_reset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextInputLayout textInputEmail = view.findViewById(R.id.fragmentPasswordReset_textInputLayout_email);
        TextInputEditText emailProvided = view.findViewById(R.id.fragmentPasswordReset_textInputEditText_email);
        Button buttonNext = view.findViewById(R.id.fragmentPasswordReset_button_next);

        emailRecoverPasswordObserver = result -> {

            if (result.isSuccessful()){

                Navigation
                        .findNavController(view)
                        .navigate(R.id.action_passwordRecoverFragment_to_loginFragment);
            }
            else {
                Snackbar.make(view, "Impossibile completare l'operazione richiesta",
                                Snackbar.LENGTH_SHORT)
                        .show();
            }
        };

        emailProvided.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String email = String.valueOf(emailProvided.getText());
                String result = userViewModel.checkEmail(email);

                switch(result){
                    case "ok":
                        textInputEmail.setError(null);
                        break;
                    case "empty":
                        textInputEmail.setError(getString(R.string.informationRequiredError));
                        break;
                    case "invalid":
                        textInputEmail.setError(getString(R.string.invalidEmail));
                        break;
                }
            }
            else {
                textInputEmail.setError(null);
            }
        });

        buttonNext.setOnClickListener(v -> {

            view.clearFocus();

            if(userViewModel.checkEmail(String.valueOf(emailProvided.getText())).equals("ok")){
                userViewModel
                        .sendResetPasswordEmail(String.valueOf(emailProvided.getText()))
                        .observe(this.requireActivity(), emailRecoverPasswordObserver);
                Snackbar.make(view, "Inviata una mail di ripristino password all'indirizzo specificato",
                        Snackbar.LENGTH_SHORT).show();
            }
            else{
                Snackbar.make(view, "Errore nella procedura di invio email.",
                                Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}