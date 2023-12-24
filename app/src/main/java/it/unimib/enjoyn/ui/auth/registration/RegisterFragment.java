package it.unimib.enjoyn.ui.auth.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.ui.UserViewModel;
import it.unimib.enjoyn.ui.auth.LoginActivity;


public class RegisterFragment extends Fragment {


    TextInputLayout textInputUsername;

    private UserViewModel userViewModel;

    private static final boolean USE_NAVIGATION_COMPONENT = true;

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

        //passaggio flat, va implementata la logica
        buttonRegister.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_confirmRegistrationCode);
        });

        //passaggio flat, va implementata la logica per i controlli
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
        //da utilizzare solo se si passa ad un'altra activity
        if (finishActivity){
            requireActivity().finish();
        }
    }
}