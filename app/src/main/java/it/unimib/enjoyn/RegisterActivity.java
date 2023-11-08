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
    Button ButtonRegister;

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Textpassword = (findViewById(R.id.insertPassword));
        ButtonRegister = findViewById(R.id.buttonRegister);
        TextEmail = findViewById(R.id.insertEmail);
        TextConfirmpassword = findViewById(R.id.confirmPassword);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = Textpassword.getEditText().getText().toString();
                String email = TextEmail.getEditText().getText().toString();
                String confirmPassword = TextConfirmpassword.getEditText().getText().toString();
                boolean checkedConfirmPassword= false;
                boolean checkedPassword = checkPassword(password);
                if(checkedPassword){
                    checkedConfirmPassword= checkConfirmPassword( password, confirmPassword);
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
        if (password != null && password.length() >= 8) {
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

        } else {
            Textpassword.setError("@string/tooShortPassword");
            return false;
        }
        if (!number) {
            Textpassword.setError("@string/numberMissingPassword");
        } else {
            if (!capLetter) {
                Textpassword.setError("@string/upperCaseMissingPassword");
            } else {
                if (!specialChar) {
                    Textpassword.setError("@string/specialCharacterMissingPassword");
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
        if (!(EmailValidator.getInstance().isValid(email))) {
            TextEmail.setError("@string/notValidEmail");
            return false;
        } else {
            TextEmail.setError(null);
            return true;
        }
    }
    private boolean checkConfirmPassword(String password, String confirmPassword) {
        if (!(confirmPassword.equals(password))) {
            TextConfirmpassword.setError("@string/PasswordNotEqual");
            return false;
        } else {
            TextConfirmpassword.setError(null);
            return true;
        }
    }
}