package it.unimib.enjoyn;

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

import org.apache.commons.validator.routines.EmailValidator;

public class LoginFragment extends Fragment {

    private static final boolean USE_NAVIGATION_COMPONENT = true;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button buttonRegister = view.findViewById(R.id.fragmentLogin_button_register);
        Button buttonLogin = view.findViewById(R.id.fragmentLogin_button_login);
        Button buttonForgottenPassword = view.findViewById(R.id.fragmentLogin_button_forgottenPassword);

        /*

        //logica da spostare altrove

        TextInputLayout textInputLayoutEmail = view.findViewById(R.id.fragmentLogin_textInputLayout_email);
        TextInputLayout textInputLayoutPassword = view.findViewById(R.id.fragmentLogin_textInputLayout_password);

        EditText editTextMail = view.findViewById(R.id.fragmentLogin_textInputEditText_email);
        EditText editTextPassword = view.findViewById(R.id.fragmentLogin_textInputEditText_password);

        //Serve a controllare che l'uente abbia inserito correttamente la mail.
        editTextMail.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){

                int result = checkEmail(editTextMail.getText().toString());

                if (result == 1)
                    textInputLayoutEmail.setError(getString(R.string.Stringnull));
                else if (result == 2)
                    textInputLayoutEmail.setError(getString(R.string.notValidEmail));
                else
                    textInputLayoutEmail.setError(null);
            }
            else {
                textInputLayoutEmail.setError(null);
            }
        });

        //Serve a controllare che l'uente abbia inserito correttamente la password.
        editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(isPasswordOk(editTextPassword.getText().toString()))
                    textInputLayoutPassword.setError(getString(R.string.Stringnull));
                else
                    textInputLayoutPassword.setError(null);
            }
            else {
                textInputLayoutPassword.setError(null);
            }
        });
         */


        buttonRegister.setOnClickListener(v -> startActivityBasedOnCondition(RegisterActivity.class,
                R.id.action_loginFragment_to_registerActivity, true));

        buttonForgottenPassword.setOnClickListener(v -> {
            startActivityBasedOnCondition(LoginActivity.class, R.id.action_loginFragment_to_passwordRecoverFragment, false);
        });

        buttonLogin.setOnClickListener(v -> {
            /*
            String password = textInputLayoutPassword.getEditText().getText().toString();
            String email = textInputLayoutEmail.getEditText().getText().toString();
            boolean checkedEmail = checkEmail(email);
            boolean checkedPassword = checkPassword(password);
             */
            startActivityBasedOnCondition(MainButtonMenuActivity.class,
                    R.id.action_loginFragment_to_mainButtonMenuActivity, true);
            /*
            if(checkedEmail && checkedPassword){
                startActivityBasedOnCondition(MainButtonMenuActivity.class,
                        R.id.action_loginFragment_to_mainButtonMenuActivity, true);
            }
             */
        });
    }

    /*
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

    /*
    private int checkEmail(String email) {
        if(email == null || email.length() == 0)
            return 1;
        if (!(EmailValidator.getInstance().isValid(email)))
            return 2;
        else
            return 0;
    }

    private boolean isPasswordOk(String password){
        return password == null || password.length() == 0;
    }
     */
}