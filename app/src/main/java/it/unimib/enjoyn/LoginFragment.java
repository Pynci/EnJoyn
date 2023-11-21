package it.unimib.enjoyn;

import static it.unimib.enjoyn.util.Costants.EMAIL_ADDRESS;
import static it.unimib.enjoyn.util.Costants.ENCRYPTED_DATA_FILE_NAME;
import static it.unimib.enjoyn.util.Costants.SHARED_PREFERENCES_FILE_NAME;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.unimib.enjoyn.util.DataEncryptionUtil;
import it.unimib.enjoyn.util.SharedPreferencesUtil;

public class LoginFragment extends Fragment {

    Button ButtonLoginToRegister;

    Button LoginButton;

    final String KEY_EMAIL="email";
    private static final boolean USE_NAVIGATION_COMPONENT = true;
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButtonLoginToRegister = view.findViewById(R.id.buttonLoginToRegister);
        LoginButton = view.findViewById(R.id.buttonLogin);


        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(requireContext());
        try {
            if (!dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME).isEmpty()) {
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(requireContext());


            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }


        ButtonLoginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityBasedOnCondition(RegisterActivity.class,
                        R.id.action_loginFragment_to_registerActivity, true);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityBasedOnCondition(MainButtonMenuActivity.class,
                        R.id.action_loginFragment_to_mainButtonMenuActivity, true);
            }
        }

        );
    }

    private void startActivityBasedOnCondition(Class<?> destinationActivity, int destination, boolean finishActivity) {
        if (USE_NAVIGATION_COMPONENT) {
            Navigation.findNavController(requireView()).navigate(destination);
        } else {
            Intent intent = new Intent(requireContext(), destinationActivity);
            startActivity(intent);
        }
        //da utilizzare solo se si passa ad un'altra activity
        if (finishActivity){
            requireActivity().finish();
        }
    }
}