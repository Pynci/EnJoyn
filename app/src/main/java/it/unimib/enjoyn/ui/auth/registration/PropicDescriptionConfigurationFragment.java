package it.unimib.enjoyn.ui.auth.registration;

import android.os.Bundle;

//classi per la gestione del caricamento immagine
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import it.unimib.enjoyn.R;

public class PropicDescriptionConfigurationFragment extends Fragment {

    public static final String TAG = PropicDescriptionConfigurationFragment.class.getSimpleName();

    public PropicDescriptionConfigurationFragment() {

    }

    public static PropicDescriptionConfigurationFragment newInstance() {
        return new PropicDescriptionConfigurationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // disabilita il tasto back affinché l'utente non possa tornare indietro
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // logica personalizzata per il tasto back (in questo caso non deve fare niente)
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_propic_description_configuration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceBundle){
        super.onViewCreated(view, savedInstanceBundle);

        ShapeableImageView userImage = view.findViewById(R.id.propicDescriptionConfiguration_imageView_propic);
        Button buttonNext = view.findViewById(R.id.propicDescriptionConfiguration_button_next);
        ImageButton imageButtonAddPropic = view.findViewById(R.id.propicDescriptionConfiguration_imageButton_addPropic);
        EditText cognome = view.findViewById(R.id.propicDescriptionConfiguration_editText_cognome);
        EditText nome = view.findViewById(R.id.propicDescriptionConfiguration_editText_nome);
        TextInputEditText description = view.findViewById(R.id.propicDescriptionConfiguration_textInputEditText_description);

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        userImage.setImageURI(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        buttonNext.setOnClickListener(v -> {

            //TODO: inserire il controllo sull'username (dal DB)
            Navigation.findNavController(v).navigate(R.id.action_propicDescriptionConfigurationFragment_to_categoriesSelectionFragment);

        });

        imageButtonAddPropic.setOnClickListener(v -> {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

    }
}