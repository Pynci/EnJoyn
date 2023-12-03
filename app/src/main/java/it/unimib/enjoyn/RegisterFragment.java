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
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    TextInputLayout Textpassword;
    TextInputLayout TextEmail;
    TextInputLayout TextConfirmpassword;
    TextInputLayout TextName;
    TextInputLayout TextSurname;
    TextInputLayout TextUsername;

    EditText Password;

    EditText Email;

    EditText Name;

    EditText Surname;

    EditText ConfirmPassword;

    EditText Username;


    Button ButtonRegister;
    Button ButtonRegisterToLogin;
    private static final boolean USE_NAVIGATION_COMPONENT = true;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /*
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Textpassword = (view.findViewById(R.id.fragmentRegister_TextInputLayout_Password));
        ButtonRegister = view.findViewById(R.id.fragmentLogin_button_PasswordDimenticata);
        ButtonRegisterToLogin = view.findViewById(R.id.fragmentLogin_button_ButtonLogin);
        TextEmail = view.findViewById(R.id.fragmentRegister_TextInputLayout_Email);
        TextConfirmpassword = view.findViewById(R.id.fragmentRegister_TextInputLayout_ConfermaPassword);
        TextUsername = view.findViewById(R.id.fragmentRegister_TextInputLayout_username);
        Password = view.findViewById(R.id.fragmentRegister_TextInputEditText_Password);
        Email = view.findViewById(R.id.fragmentRegister_TextInputEditText_Email);
        ConfirmPassword = view.findViewById(R.id.fragmentRegister_TextInputEditText_ConfermaPassword);
        Username = view.findViewById(R.id.fragmentRegister_TextInputEditText_Username);

       Email.setOnFocusChangeListener((v, hasFocus) -> {
           if(!hasFocus){
               String email = Email.getText().toString();
               checkEmail(email);
           }
           else {
               TextEmail.setError(null);
           }
       });

        Password.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = Password.getText().toString();
                checkPassword(password);
            }
            else {
                Textpassword.setError(null);
            }
        });

        ConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = Password.getText().toString();
                String confirmPassword = ConfirmPassword.getText().toString();
                checkConfirmPassword(password, confirmPassword);
            }
            else {
                TextConfirmpassword.setError(null);
            }
        });

        Username.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String username = Username.getText().toString();
                checkUsername(username);
            }
            else {
                TextUsername.setError(null);
            }
        });



        ButtonRegister.setOnClickListener(v -> {

            /*

            String name = TextName.getEditText().getText().toString();
            String surname = TextSurname.getEditText().getText().toString();
            String password = Textpassword.getEditText().getText().toString();
            String email = TextEmail.getEditText().getText().toString();
            String confirmPassword = TextConfirmpassword.getEditText().getText().toString();
            String username = TextUsername.getEditText().getText().toString();
            String phoneNumber = TextPhoneNumber.getEditText().getText().toString();
            boolean checkedConfirmPassword = checkConfirmPassword(password, confirmPassword);;
            boolean checkedName = checkSurName(name, TextName);
            boolean checkedSurname = checkSurName(surname, TextSurname);
            boolean checkedPassword = checkPassword(password);
            boolean checkedUsername = checkUsername(username);
            boolean checkedPhoneNumber = checkPhoneNumber(phoneNumber);

            if(checkedPassword){
                checkedConfirmPassword = checkConfirmPassword(password, confirmPassword);
            }



            boolean checkedEmail = checkEmail(email);

            */
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_confirmRegistrationCode);
        });

        ButtonRegisterToLogin.setOnClickListener(v -> startActivityBasedOnCondition(LoginActivity.class,
                R.id.action_registerFragment_to_loginActivity, true));
    }

        private boolean checkPassword(String password) {
        boolean passedP;
        boolean number = false;
        boolean specialChar = false;
        boolean capLetter = false;
        if (password == null || password.length()==0) {
            Textpassword.setError(getString(R.string.stringNull));
            return false;
        }
        if(password.length() < 8) {
            Textpassword.setError(getString(R.string.tooShortPassword));
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
            Textpassword.setError(getString(R.string.numberMissingPassword));
        } else {
            if (!capLetter) {
                Textpassword.setError(getString(R.string.upperCaseMissingPassword));
            } else {
                if (!specialChar) {
                    Textpassword.setError(getString(R.string.specialCharacterMissingPassword));
                }
            }

        }
        passedP = (number && capLetter && specialChar);
        if (passedP) {
            Textpassword.setError(null);
        }
        return passedP;
    }

    private boolean checkEmail(String email) {
        if(email==null || email.length()==0) {
            TextEmail.setError(getString(R.string.stringNull));
            return false;
        }
        if (!(EmailValidator.getInstance().isValid(email))) {
            TextEmail.setError(getString(R.string.notValidEmail));
            return false;
        } else {
            TextEmail.setError(null);
            return true;
        }
    }
    private boolean checkConfirmPassword(String password, String confirmPassword) {
        if(confirmPassword==null || confirmPassword.length()==0) {
            TextConfirmpassword.setError(getString(R.string.stringNull));
            return false;
        }
        if (!(confirmPassword.equals(password))) {
            TextConfirmpassword.setError(getString(R.string.passwordNotEqual));
            return false;
        } else {
            TextConfirmpassword.setError(null);
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
            TextUsername.setError(getString(R.string.stringNull));
            return false;}
        if(username.length()>=20) {
            TextUsername.setError(getString(R.string.stringTooLong));
            return false;
        }
        //aggiungere controllo di esistenza sul db

        TextUsername.setError(null);
        return true;
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