package it.unimib.enjoyn.ui.auth.registration;

// TODO: capire se è giusto che venga fuori questa caterva di dipendenze

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.enjoyn.R;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.ui.UserViewModel;
import it.unimib.enjoyn.ui.auth.LoginActivity;


public class RegisterFragment extends Fragment {

    public static final String TAG = RegisterFragment.class.getSimpleName();
    private UserViewModel userViewModel;
    private static final boolean USE_NAVIGATION_COMPONENT = true;
    private Observer<Result> addUserObserver;
    private Observer<Result> usernameCheckObserver;
    private Observer<Result> emailCheckObserver;
    private boolean isUsernameOK = false;
    private boolean isEmailOK = false;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Widgets

        Button buttonRegister = view.findViewById(R.id.fragmentRegister_button_register);
        Button buttonLogin = view.findViewById(R.id.fragmentRegister_button_login);

        TextInputLayout textInputEmail = view.findViewById(R.id.fragmentRegister_textInputLayout_email);
        EditText editTextEmail = view.findViewById(R.id.fragmentRegister_textInputEditText_email);

        TextInputLayout textInputPassword = view.findViewById(R.id.fragmentRegister_textInputLayout_password);
        EditText editTextPassword = view.findViewById(R.id.fragmentRegister_textInputEditText_password);

        TextInputLayout textInputConfirmPassword = view.findViewById(R.id.fragmentRegister_textInputLayout_confirmPassword);
        EditText editTextConfirmPassword = view.findViewById(R.id.fragmentRegister_textInputEditText_confirmPassword);

        TextInputLayout textInputUsername = view.findViewById(R.id.fragmentRegister_textInputLayout_username);
        EditText editTextUsername = view.findViewById(R.id.fragmentRegister_textInputEditText_username);



        //Observers

        addUserObserver = error -> {
            if(error == null){

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                firebaseUser.sendEmailVerification().addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {
                        Navigation
                                .findNavController(view)
                                .navigate(R.id.action_registerFragment_to_confirmEmailMessageFragment2);

                        Snackbar.make(view, "Registrazione avvenuta correttamente", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                    else{
                        Snackbar.make(view, "Errore nell'invio della mail di conferma", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });

            }
            else{
                Snackbar.make(view, "Errore nella registrazione: " + ((Result.Error) error).getMessage(),
                        Snackbar.LENGTH_SHORT).show();
            }
        };

        usernameCheckObserver = result -> {
            if(result.isSuccess()){
                User user = ((Result.UserResponseSuccess) result).getData();
                if(user == null){
                    isUsernameOK = true;
                }
                else{
                    textInputUsername.setError(getString(R.string.usernameAlreadyInUse));
                }
            }
            else{
                Snackbar.make(view, "Si è verificato un errore: " + ((Result.Error) result).getMessage(), Snackbar.LENGTH_SHORT)
                        .show();
            }
        };

        emailCheckObserver = result -> {
            if(result.isSuccess()){
                User user = ((Result.UserResponseSuccess) result).getData();
                if(user == null){
                    isEmailOK = true;
                }
                else{
                    textInputEmail.setError(getString(R.string.emailAlreadyInUse));
                }
            }
            else{
                Snackbar.make(view, "Si è verificato un errore: " + ((Result.Error) result).getMessage(), Snackbar.LENGTH_SHORT)
                        .show();
            }
        };



        //Listeners

        editTextUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String username = String.valueOf(editTextUsername.getText());
                String result = userViewModel.checkUsername(username);

                switch(result){
                    case "ok":
                        textInputUsername.setError(null);
                        break;
                    case "empty":
                        textInputUsername.setError(getString(R.string.informationRequiredError));
                        break;
                    case "too_long":
                        textInputUsername.setError(getString(R.string.stringTooLong));
                        break;
                    case "has_whitespace":
                        textInputUsername.setError(getString(R.string.whitespaceNotAllowed));
                        break;
                }

                userViewModel.getUserByUsername(username).observe(getViewLifecycleOwner(), usernameCheckObserver);
            }
            else{
                textInputUsername.setError(null);
            }
        });

        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String email = String.valueOf(editTextEmail.getText());
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

                userViewModel.getUserByEmail(email).observe(getViewLifecycleOwner(), emailCheckObserver);
            }
            else {
                textInputEmail.setError(null);
            }
        });

        editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = String.valueOf(editTextPassword.getText());
                String result = userViewModel.checkPassword(password);

                switch(result){
                    case "ok":
                        textInputPassword.setError(null);
                        break;
                    case "empty":
                        textInputPassword.setError(getString(R.string.tooShortPassword));
                        break;
                    case "number_missing":
                        textInputPassword.setError(getString(R.string.numberMissingPassword));
                        break;
                    case "uppercaseChar_missing":
                        textInputPassword.setError(getString(R.string.upperCaseMissingPassword));
                        break;
                    case "specialChar_missing":
                        textInputPassword.setError(getString(R.string.specialCharacterMissingPassword));
                        break;
                }
            }
            else{
                textInputPassword.setError(null);
            }
        });

        editTextConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = String.valueOf(editTextPassword.getText());
                String confirmPassword = String.valueOf(editTextConfirmPassword.getText());
                String result = userViewModel.checkConfirmPassword(password, confirmPassword);

                switch(result){
                    case "ok":
                        textInputConfirmPassword.setError(null);
                        break;
                    case "empty":
                        textInputConfirmPassword.setError(getString(R.string.informationRequiredError));
                        break;
                    case "not_equal":
                        textInputConfirmPassword.setError(getText(R.string.passwordNotEqual));
                        break;
                }
            }
            else{
                textInputConfirmPassword.setError(null);
            }
        });

        buttonRegister.setOnClickListener(v -> {

            view.clearFocus();

            if(textInputEmail.getError() == null && textInputPassword.getError() == null
                && textInputConfirmPassword.getError() == null && textInputUsername.getError() == null){

                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());
                String username = String.valueOf(editTextUsername.getText());

                if(isUsernameOK && isEmailOK){
                    Log.d(this.getClass().getSimpleName(), "dentro all'if");
                    userViewModel.addUser(email, password, username).observe(getViewLifecycleOwner(), addUserObserver);
                }
                else if(!isUsernameOK){
                    Snackbar.make(view, "Errore nella registrazione: nome utente già in uso",
                            Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(view, "Errore nella registrazione: email già in uso",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

        });

        buttonLogin.setOnClickListener(v -> startActivityBasedOnCondition(LoginActivity.class,
                R.id.action_registerFragment_to_loginActivity, true));
    }

    private void startActivityBasedOnCondition(Class<?> destinationActivity, int destination, boolean finishActivity) {
        if (USE_NAVIGATION_COMPONENT) {
            Navigation.findNavController(requireView()).navigate(destination);
        } else {
            Intent intent = new Intent(requireContext(), destinationActivity);
            startActivity(intent);
        }
        if (finishActivity){
            requireActivity().finish();
        }
    }
}