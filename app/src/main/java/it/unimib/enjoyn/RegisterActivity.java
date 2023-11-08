package it.unimib.enjoyn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout Textpassword;
    TextInputLayout TextEmail;
    TextInputLayout TextConfirmpassword;
    TextInputLayout TextName;
    TextInputLayout TextSurname;
    Button ButtonRegister;

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Textpassword = (findViewById(R.id.insertPassword));
        ButtonRegister = findViewById(R.id.buttonRegister);
        TextEmail = findViewById(R.id.insertEmail);
        TextName = findViewById(R.id.insertName);
        TextSurname = findViewById(R.id.insertSurname);
        TextConfirmpassword = findViewById(R.id.confirmPassword);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = TextName.getEditText().getText().toString();
                String surname = TextSurname.getEditText().getText().toString();
                String password = Textpassword.getEditText().getText().toString();
                String email = TextEmail.getEditText().getText().toString();
                String confirmPassword = TextConfirmpassword.getEditText().getText().toString();
                boolean checkedConfirmPassword = false;
                boolean checkedName = checkSurName(name, TextName);
                boolean checkedSurname = checkSurName(surname, TextSurname);
                boolean checkedPassword = checkPassword(password);
                if(checkedPassword){
                    checkedConfirmPassword = checkConfirmPassword(password, confirmPassword);
                }
                boolean checkedEmail = checkEmail(email);
                Log.d(TAG, "funziona" + checkedPassword);
                Log.d(TAG, "funzionaE" + checkedEmail);
            }
        });
    }


    private boolean checkPassword(String password) {
        boolean passedP;
        boolean number = false;
        boolean specialChar = false;
        boolean capLetter = false;
        if (password == null || password.length()==0) {
            Textpassword.setError(getString(R.string.Stringnull));
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
            TextEmail.setError(getString(R.string.Stringnull));
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
            TextConfirmpassword.setError(getString(R.string.Stringnull));
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
            Text.setError(getString(R.string.Stringnull));
            return false;}
        if( name.length()>=42) {
            Text.setError(getString(R.string.StringTooLong));
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

}