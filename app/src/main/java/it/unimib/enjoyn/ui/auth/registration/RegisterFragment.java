package it.unimib.enjoyn.ui.auth.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.ui.auth.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    /*

    //questi fanno parte della logica che va spostata altrove

    TextInputLayout textInputPassword;
    TextInputLayout textInputEmail;
    TextInputLayout textInputConfirmPassword;
    TextInputLayout textInputUsername;

    EditText editTextPassword;
    EditText editTextEmail;
    EditText editTextConfirmPassword;
    EditText editTextUsername;
     */
    Button buttonRegister;
    Button buttonRegisterToLogin;

    private static final boolean USE_NAVIGATION_COMPONENT = true;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonRegister = view.findViewById(R.id.fragmentRegister_button_register);
        buttonRegisterToLogin = view.findViewById(R.id.fragmentRegister_button_login);

        /*

        //questa logica va spostata altrove

        textInputPassword = (view.findViewById(R.id.fragmentRegister_textInputLayout_password));
        textInputEmail = view.findViewById(R.id.fragmentRegister_textInputLayout_email);
        textInputConfirmPassword = view.findViewById(R.id.fragmentRegister_textInputLayout_confirmPassword);
        textInputUsername = view.findViewById(R.id.fragmentRegister_textInputLayout_username);
        editTextPassword = view.findViewById(R.id.fragmentRegister_textInputEditText_password);
        editTextEmail = view.findViewById(R.id.fragmentRegister_textInputEditText_email);
        editTextConfirmPassword = view.findViewById(R.id.fragmentRegister_textInputEditText_confirmPassword);
        editTextUsername = view.findViewById(R.id.fragmentRegister_textInputEditText_username);

       editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
           if(!hasFocus){
               String email = editTextEmail.getText().toString();
               checkEmail(email);
           }
           else {
               textInputEmail.setError(null);
           }
       });

        editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = editTextPassword.getText().toString();
                checkPassword(password);
            }
            else {
                textInputPassword.setError(null);
            }
        });

        editTextConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                checkConfirmPassword(password, confirmPassword);
            }
            else {
                textInputConfirmPassword.setError(null);
            }
        });

        editTextUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String username = editTextUsername.getText().toString();
                checkUsername(username);
            }
            else {
                textInputUsername.setError(null);
            }
        });

         */

        //passaggio flat, va implementata la logica
        buttonRegister.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_confirmRegistrationCode);
        });

        //passaggio flat, va implementata la logica per i controlli
        buttonRegisterToLogin.setOnClickListener(v -> startActivityBasedOnCondition(LoginActivity.class,
                R.id.action_registerFragment_to_loginActivity, true));
    }

    /*

    //questa logica va spostata altrove

        private boolean checkPassword(String password) {
            boolean passedP;
            boolean number = false;
            boolean specialChar = false;
            boolean capLetter = false;
        if (password == null || password.length()==0) {
            textInputPassword.setError(getString(R.string.stringNull));
            return false;
        }
        if(password.length() < 8) {
            textInputPassword.setError(getString(R.string.tooShortPassword));
            return false;
        }

        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) >= '0' && password.charAt(i) <= '9') {
                number = true;
            }
            if (password.charAt(i) >= 'A' && password.charAt(i) <= 'Z') {
                capLetter = true;
            }
            if (password.charAt(i) >= '!' && password.charAt(i) <= '/') {
                specialChar = true;
            }

        }


        if (!number) {
            textInputPassword.setError(getString(R.string.numberMissingPassword));
        } else {
            if (!capLetter) {
                textInputPassword.setError(getString(R.string.upperCaseMissingPassword));
            } else {
                if (!specialChar) {
                    textInputPassword.setError(getString(R.string.specialCharacterMissingPassword));
                }
            }

        }
        passedP = (number && capLetter && specialChar);
        if (passedP) {
            textInputPassword.setError(null);
        }
        return passedP;
    }

    private boolean checkEmail(String email) {
        if(email==null || email.length()==0) {
            textInputEmail.setError(getString(R.string.stringNull));
            return false;
        }
        if (!(EmailValidator.getInstance().isValid(email))) {
            textInputEmail.setError(getString(R.string.notValidEmail));
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }
    private boolean checkConfirmPassword(String password, String confirmPassword) {
        if(confirmPassword==null || confirmPassword.length()==0) {
            textInputConfirmPassword.setError(getString(R.string.stringNull));
            return false;
        }
        if (!(confirmPassword.equals(password))) {
            textInputConfirmPassword.setError(getString(R.string.passwordNotEqual));
            return false;
        } else {
            textInputConfirmPassword.setError(null);
            return true;
        }
    }
    private boolean checkSurName(String name, TextInputLayout Text) {

        if (name == null || name.length()==0) {
            Text.setError(getString(R.string.stringNull));
            return false;}
        if( name.length()>=42) {
            Text.setError(getString(R.string.stringTooLong));
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (!(name.charAt(i) >= 'A' && name.charAt(i) <= 'Z') && !(name.charAt(i) >= 'a' && name.charAt(i) <= 'z') && !(name.charAt(i) == 32)) {
                Text.setError(getString(R.string.notSerious));
                return false;
            }
        }
        Text.setError(null);
        return true;

    }

    private boolean checkUsername(String username){
        if (username == null || username.length()==0) {
            textInputUsername.setError(getString(R.string.stringNull));
            return false;}
        if(username.length()>=20) {
            textInputUsername.setError(getString(R.string.stringTooLong));
            return false;
        }
        //aggiungere controllo di esistenza sul db

        textInputUsername.setError(null);
        return true;
    }

     */

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