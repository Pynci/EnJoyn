package it.unimib.enjoyn.ui.auth.registration;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

//classi per la gestione del caricamento immagine
import androidx.activity.OnBackPressedCallback;
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

import it.unimib.enjoyn.R;

public class PropicDescriptionConfigurationFragment extends Fragment {

    public static final String TAG = PropicDescriptionConfigurationFragment.class.getSimpleName();
    private static final boolean USE_NAVIGATION_COMPONENT = true;


    //callback settata per ricevere la foto selezionata dalla galleria (minuto 23 esercitazione Intent)
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if(uri != null){
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
                        ((ImageView) getView().findViewById(R.id.propicDescriptionConfiguration_imageView_propic)).setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });



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


        final Button buttonNext = view.findViewById(R.id.propicDescriptionConfiguration_button_next);
        final ImageButton imageButtonAddPropic = view.findViewById(R.id.propicDescriptionConfiguration_imageButton_addPropic);

        /*

       //per chiunque stia leggendo: se decommenti queste istruzioni crasha tutto e ti viene
       //un gigantesco tumore ai polmoni (incurabile)

        int themeColor = ContextCompat.getColor(requireContext(), requireContext().getTheme().getChangingConfigurations());
        Drawable propic = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_account_circle_24);
        Drawable addPropic = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_add_circle_24);
        */


        //questi potrebbero in futuro servire come attributi del fragment
        EditText username = view.findViewById(R.id.propicDescriptionConfiguration_editText_cognome);
        TextInputEditText description = view.findViewById(R.id.propicDescriptionConfiguration_textInputEditText_description);



        buttonNext.setOnClickListener(v -> {

            //TODO: inserire il controllo sull'username (dal DB)
            Navigation.findNavController(v).navigate(R.id.action_propicDescriptionConfigurationFragment_to_categoriesSelectionFragment);

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
    all'altra) definita.
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