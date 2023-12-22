package it.unimib.enjoyn.ui.auth.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.source.user.UserRemoteDataSource;
import it.unimib.enjoyn.util.ServiceLocator;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

}