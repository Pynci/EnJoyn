package it.unimib.enjoyn.ui.auth.registration;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import it.unimib.enjoyn.R;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.UserViewModelFactory;
import it.unimib.enjoyn.util.ServiceLocator;
import it.unimib.enjoyn.util.SnackbarBuilder;
import it.unimib.enjoyn.ui.UserViewModel;

// TODO: sistemare la presentazione degli errori all'utente ed eventuali stringhe hardcodate
public class RegisterFragment extends Fragment {

    private UserViewModel userViewModel;
    private Observer<Result> signUpObserver;
    private Observer<Result> emailVerificationSendingObserver;
    private Observer<Result> usernameCheckObserver;
    private Observer<Result> emailCheckObserver;
    private boolean isUsernameOK;
    private boolean isEmailOK;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        isUsernameOK = false;
        isEmailOK = false;
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

        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        //Observers

        signUpObserver = result -> {
            if(result.isSuccessful()){
                userViewModel.sendEmailVerification().observe(getViewLifecycleOwner(), emailVerificationSendingObserver);
            }
            else{
                String text = "Errore nella registrazione: " + ((Result.Error) result).getMessage();
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(text, view, getContext(), currentTheme);
                snackbar.show();
            }
        };

        emailVerificationSendingObserver = result -> {
            if(result.isSuccessful()){
                navigateTo(R.id.action_registerFragment_to_confirmEmailMessageFragment2, false);

                String text = "Registrazione avvenuta correttamente";
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildOkSnackbar(text, view, getContext(), currentTheme);
                snackbar.show();
            }
            else{
                String text = "Errore nell'invio della mail di conferma";
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(text, view, getContext(), currentTheme);
                snackbar.show();
            }
        };

        usernameCheckObserver = result -> {
            if(result.isSuccessful()){
                User user = ((Result.UserSuccess) result).getData();
                if(user == null){
                    isUsernameOK = true;
                }
                else{
                    textInputUsername.setError(getString(R.string.usernameAlreadyInUse));
                }
            }
            else{
                String text = "Si è verificato un errore: " + ((Result.Error) result).getMessage();
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(text, view, getContext(), currentTheme);
                snackbar.show();
            }
        };

        emailCheckObserver = result -> {
            if(result.isSuccessful()){
                User user = ((Result.UserSuccess) result).getData();
                if(user == null){
                    isEmailOK = true;
                }
                else{
                    textInputEmail.setError(getString(R.string.emailAlreadyInUse));
                }
            }
            else{
                String text = "Si è verificato un errore: " + ((Result.Error) result).getMessage();
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(text, view, getContext(), currentTheme);
                snackbar.show();
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

            String username = String.valueOf(editTextUsername.getText());
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());
            String confirmPassword = String.valueOf(editTextPassword.getText());

            if(userViewModel.checkUsername(username).equals("ok") &&
                    userViewModel.checkEmail(email).equals("ok") &&
                    userViewModel.checkPassword(password).equals("ok") &&
                    userViewModel.checkConfirmPassword(password, confirmPassword).equals("ok")){

                if(isUsernameOK && isEmailOK){
                    userViewModel.signUp(email, password, username).observe(getViewLifecycleOwner(), signUpObserver);
                }
                else if(!isUsernameOK){

                    String text = "Username già in uso";
                    Snackbar snackbar;
                    snackbar = SnackbarBuilder.buildErrorSnackbar(text, view, getContext(), currentTheme);
                    snackbar.show();
                }
                else {
                    String text = "Email già in uso";
                    Snackbar snackbar;
                    snackbar = SnackbarBuilder.buildErrorSnackbar(text, view, getContext(), currentTheme);
                    snackbar.show();
                }
            }
            else{
                String text = "Registrazione fallita";
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(text, view, getContext(), currentTheme);
                snackbar.show();
            }

        });

        buttonLogin.setOnClickListener(v -> navigateTo(
                R.id.action_registerFragment_to_loginActivity, true));
    }

    private void navigateTo(int destination, boolean finishActivity) {
        Navigation.findNavController(requireView()).navigate(destination);
        if (finishActivity){
            requireActivity().finish();
        }
    }
}