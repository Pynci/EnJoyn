package it.unimib.enjoyn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

//classi per la gestione del caricamento immagine
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PropicDescriptionConfigurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PropicDescriptionConfigurationFragment extends Fragment {

    public static final String TAG = PropicDescriptionConfigurationFragment.class.getSimpleName();
    private static final boolean USE_NAVIGATION_COMPONENT = true;

    //callback settata per ricevere la foto selezionata dalla galleria (minuto 23 esercitazione Intent)
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
                        ((ImageView) getView().findViewById(R.id.imageView_propic)).setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });



    public PropicDescriptionConfigurationFragment() {
        // Required empty public constructor
    }

    //factory method per ottenere istanze del fragment
    public static PropicDescriptionConfigurationFragment newInstance() {
        return new PropicDescriptionConfigurationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_propic_description_configuration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceBundle){
        super.onViewCreated(view, savedInstanceBundle);


        final Button buttonNext = view.findViewById(R.id.button_next);
        final ImageButton imageButtonAddPropic = view.findViewById(R.id.imageButton_addPropic);

        //questi potrebbero in futuro servire come attributi del fragment
        EditText username = view.findViewById(R.id.editText_username);
        TextInputEditText description = view.findViewById(R.id.textInputEditText_description);



        buttonNext.setOnClickListener(v -> {

            //TODO: inserire il controllo sull'username (dal DB)

            //TODO: capire come cazzo si passa da un fragment all'altro. Serve activity?
        });

        imageButtonAddPropic.setOnClickListener(v -> {

            //TODO: implementare la logica di caricamento dell'immagine in relazione con il DB
            //TODO: manipolare l'immagine in modo che non si smerdi
            mGetContent.launch("image/*");
        });

    }

    /*
    Avvia un'activity utilizzando gli Intent oppure il NavigationComponent.
    Prende in input la classe dell'activity da avviare e l'id associato all'azione (passaggio da un'activity
    all'altra) definita in nav_first_profile_configuration.xml.
    */
    private void startActivityBasedOnCondition(Class<?> destinationActivity, int destination) {
        if (USE_NAVIGATION_COMPONENT) {
            Navigation.findNavController(requireView()).navigate(destination);
        } else {
            Intent intent = new Intent(requireContext(), destinationActivity);
            startActivity(intent);
        }
        requireActivity().finish();
    }
}