package it.unimib.enjoyn.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.ui.UserViewModel;
import it.unimib.enjoyn.ui.main.MainButtonMenuActivity;
import it.unimib.enjoyn.R;
import it.unimib.enjoyn.ui.auth.registration.RegisterActivity;

public class LoginFragment extends Fragment {

    private static final boolean USE_NAVIGATION_COMPONENT = true;
    private UserViewModel userViewModel;
    private Observer<Result> signInObserver;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Widgets

        Button buttonRegister = view.findViewById(R.id.fragmentLogin_button_register);
        Button buttonLogin = view.findViewById(R.id.fragmentLogin_button_login);
        Button buttonForgottenPassword = view.findViewById(R.id.fragmentLogin_button_forgottenPassword);

        TextInputLayout textInputEmail = view.findViewById(R.id.fragmentLogin_textInputLayout_email);
        EditText editTextEmail = view.findViewById(R.id.fragmentLogin_textInputEditText_email);

        TextInputLayout textInputPassword = view.findViewById(R.id.fragmentLogin_textInputLayout_password);
        EditText editTextPassword = view.findViewById(R.id.fragmentLogin_textInputEditText_password);


        // Observers

        signInObserver = result -> {
            if(result.isSuccess()){

                Navigation
                        .findNavController(view)
                        .navigate(R.id.action_loginFragment_to_propicDescriptionConfigurationFragment);
                Snackbar.make(view, "(TEST) Login effettuato, mail: "
                                        + ((Result.SignInSuccess) result).getData().getEmail(),
                                Snackbar.LENGTH_SHORT)
                        .show();

//                // da includere dopo aver sistemato la configurazione del profilo
//                Snackbar.make(view, "Bentornato " + auth.getCurrentUser().getDisplayName(), Snackbar.LENGTH_SHORT)
//                        .show();
            }
            else{
                Snackbar.make(view, getString(R.string.authenticationFailed),
                        Snackbar.LENGTH_SHORT).show();
            }
        };


        // Listeners

        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String email = String.valueOf(editTextEmail.getText());
                String result = userViewModel.checkEmail(email);

                if (result.equals("empty")) {
                    textInputEmail.setError(getString(R.string.informationRequiredError));
                }
                else {
                    textInputEmail.setError(null);
                }
            }
            else {
                textInputEmail.setError(null);
            }
        });

        editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = String.valueOf(editTextPassword.getText());
                String result = userViewModel.checkPassword(password);

                if (result.equals("empty")) {
                    textInputPassword.setError(getString(R.string.informationRequiredError));
                }
                else {
                    textInputPassword.setError(null);
                }
            }
            else{
                textInputPassword.setError(null);
            }
        });


        buttonRegister.setOnClickListener(v -> startActivityBasedOnCondition(RegisterActivity.class,
                R.id.action_loginFragment_to_registerActivity, true));

        buttonForgottenPassword.setOnClickListener(v -> {
            startActivityBasedOnCondition(LoginActivity.class, R.id.action_loginFragment_to_passwordRecoverFragment, false);
        });

        buttonLogin.setOnClickListener(v -> {

            view.clearFocus();

            if(textInputEmail.getError() == null && textInputPassword.getError() == null){

                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());

                userViewModel.signIn(email, password).observe(getViewLifecycleOwner(), signInObserver);
            }

        });
    }

    /*
        // TODO: capire se sta roba serve o meno

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(requireContext());
        try {
            if (!dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME).isEmpty()) {
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(requireContext());
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
         */

    private void startActivityBasedOnCondition(Class<?> destinationActivity, int destination,
                                               boolean finishActivity) {
        if (USE_NAVIGATION_COMPONENT) {
            Navigation.findNavController(requireView()).navigate(destination);
        } else {
            Intent intent = new Intent(requireContext(), destinationActivity);
            startActivity(intent);
        }
        //da utilizzare solo se si passa ad un'altra activity
        if (finishActivity) {
            requireActivity().finish();
        }
    }
}