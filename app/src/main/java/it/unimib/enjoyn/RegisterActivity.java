package it.unimib.enjoyn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout Textpassword;
    Button ButtonRegister;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Textpassword = (findViewById(R.id.insertPassword));
        ButtonRegister= findViewById(R.id.buttonRegister);
        ButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = Textpassword.getEditText().getText().toString();
                boolean checkedPassword = checkPassword(password);
                Log.d(TAG, "funziona"+checkedPassword);
            }
        });
    }



    private boolean checkPassword(String password){
        boolean passedP;
        boolean number = false;
        boolean specialChar = false;
        boolean capLetter = false;
        if( password != null && password.length() >= 8){
            for(int i = 0; i < password.length() ; i++ ){
                if(password.charAt(i) >= '0' && password.charAt(i) <= '9'){
                    number = true;
                }
                if(password.charAt(i) >= 'A' && password.charAt(i) <= 'Z'){
                    capLetter = true;
                }
                if(password.charAt(i) >= '!' && password.charAt(i) <= '/'){
                    specialChar = true;
                }

            }

        }
        else{
            Textpassword.setError("@string/tooShortPassword");
            return false;
        }
        if(!number){
            Textpassword.setError("@string/numberMissingPassword");
        }
        else {
            if(!capLetter){
                Textpassword.setError("@string/upperCaseMissingPassword");
            }
            else{
                if(!specialChar){
                    Textpassword.setError("@string/specialCharacterMissingPassword");
                }
            }

        }
        passedP= (number && capLetter && specialChar);
        if(passedP){
        Textpassword.setError(null);
        }
        return passedP;
    }
}