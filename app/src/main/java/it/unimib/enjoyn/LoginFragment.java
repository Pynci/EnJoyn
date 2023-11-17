package it.unimib.enjoyn;

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

public class LoginFragment extends Fragment {

    Button ButtonLoginToRegister;

    Button LoginButton;
    private static final boolean USE_NAVIGATION_COMPONENT = true;

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
        ButtonLoginToRegister = view.findViewById(R.id.buttonLoginToRegister);
        LoginButton = view.findViewById(R.id.buttonLogin);
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