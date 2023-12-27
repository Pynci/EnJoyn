package it.unimib.enjoyn.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mapbox.maps.MapboxMap;

import it.unimib.enjoyn.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}