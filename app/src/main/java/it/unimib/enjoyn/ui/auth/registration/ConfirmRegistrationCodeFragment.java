package it.unimib.enjoyn.ui.auth.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.chaos.view.PinView;

import it.unimib.enjoyn.R;

public class ConfirmRegistrationCodeFragment extends Fragment {


    public ConfirmRegistrationCodeFragment() {
        // Required empty public constructor
    }

    public static ConfirmRegistrationCodeFragment newInstance() {
        return new ConfirmRegistrationCodeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_registration_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceBundle) {
        super.onViewCreated(view, savedInstanceBundle);

        PinView pinViewOTPcode = view.findViewById(R.id.fragmentConfirmRegistrationCode_PinView_OTPcode);

        pinViewOTPcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Non deve fare nulla prima di cambiare il testo
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                /*
                 * TODO: modificare il codice seguente quando avremo il DB
                 * Quando l'utente ha inserito 6 numeri, il sitema dovrà interrogare il DB per
                 * verificare che l'OTP code inserito sia corretto. In tal caso l'utente potrà
                 * accedere alla fase di customizzazione del profilo; altrimenti no.
                 * Il sistema dovrà anche ridurre il numero massimo di tentativi di un'unità
                 * nel caso in cui l'utente inserisca il codice sbagliato; inoltre deve
                 * essere cancellato ciò che l'utente ha scritto nel PinView.
                 */

                if (s.toString().length() == 6){
                    //Navigation.findNavController(view).navigate(R.id.action_confirmRegistrationCode_to_propicDescriptionConfigurationFragment);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Non deve fare nulla dopo che il testo è stato cambiato
            }
        });
    }
}