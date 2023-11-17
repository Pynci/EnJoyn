package it.unimib.enjoyn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

public class RegisterProfileActivity extends AppCompatActivity {

    private static final String TAG = RegisterProfileActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        final ImageButton aggiungiPropic = findViewById(R.id.button_addPropic);

        aggiungiPropic.setOnClickListener(ocl -> {
            //qui dentro dobbiamo mettere la logica applicativa per caricare la foto
            Log.d(TAG, "funzica diahane");
        });
    }
}