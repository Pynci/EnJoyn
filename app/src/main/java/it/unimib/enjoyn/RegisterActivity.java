package it.unimib.enjoyn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout Textpassword;
    Button ButtonRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Textpassword = (findViewById(R.id.insertPassword));
       // ButtonRegister= findViewById(R.id.button)
        boolean checkedPassword = checkPassword(Textpassword.getEditText().getText().toString());
    }

   // public void setOnClickListener(new )




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