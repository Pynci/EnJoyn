package it.unimib.enjoyn.ui.auth;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;
import it.unimib.enjoyn.R;
import it.unimib.enjoyn.ui.viewmodels.UserViewModelFactory;
import it.unimib.enjoyn.util.ServiceLocator;

public class SigninFragment extends Fragment {

    private UserViewModel userViewModel;
    private Observer<Result> signInObserver;

    public static SigninFragment newInstance() {
        return new SigninFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        userViewModel = new ViewModelProvider(requireActivity(),
                new UserViewModelFactory(userRepository)).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Widgets

        Button buttonRegister = view.findViewById(R.id.fragmentLogin_button_register);
        Button buttonLogin = view.findViewById(R.id.fragmentLogin_button_login);
        Button buttonForgottenPassword = view.findViewById(R.id.fragmentLogin_button_forgottenPassword);

        TextInputLayout textInputEmail = view.findViewById(R.id.fragmentLogin_textInputLayout_email);
        EditText editTextEmail = view.findViewById(R.id.fragmentLogin_textInputEditText_email);

        TextInputLayout textInputPassword = view.findViewById(R.id.fragmentLogin_textInputLayout_password);
        EditText editTextPassword = view.findViewById(R.id.fragmentLogin_textInputEditText_password);

        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // Observers

        signInObserver = result -> {
            if(result.isSuccessful()){
                User currentUser = ((Result.UserSuccess) result).getData();
                if(currentUser != null){
                    if(!currentUser.getEmailVerified()){
                        navigateTo(R.id.action_signinFragment_to_emailVerificationFragment, false);
                    }
                    else if(!currentUser.getProfileConfigured()){
                        navigateTo(R.id.action_signinFragment_to_profileConfigurationFragment, false, false);
                    }
                    else if(!currentUser.getCategoriesSelectionDone()){
                        navigateTo(R.id.action_signinFragment_to_categoriesSelectionFragment, false, false);
                    }
                    else{
                        navigateTo(R.id.action_signinFragment_to_mainButtonMenuActivity, true);
                    }
                }
            }
            else{
                Snackbar.make(view, getString(R.string.authenticationFailed),
                        Snackbar.LENGTH_SHORT).show();
            }
        };


        // Listeners

        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String email = String.valueOf(editTextEmail.getText());
                String result = userViewModel.checkEmail(email);

                if (result.equals("empty")) {
                    textInputEmail.setError(getString(R.string.informationRequiredError));
                }
                else {
                    textInputEmail.setError(null);
                }
            }
            else {
                textInputEmail.setError(null);
            }
        });

        editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = String.valueOf(editTextPassword.getText());
                String result = userViewModel.checkPassword(password);

                if (result.equals("empty")) {
                    textInputPassword.setError(getString(R.string.informationRequiredError));
                }
                else {
                    textInputPassword.setError(null);
                }
            }
            else{
                textInputPassword.setError(null);
            }
        });


        buttonRegister.setOnClickListener(v -> navigateTo(
                R.id.action_signinFragment_to_signupFragment, false));

        buttonForgottenPassword.setOnClickListener(v -> navigateTo(
                R.id.action_signinFragment_to_passwordResetFragment, false));

        buttonLogin.setOnClickListener(v -> {

            view.clearFocus();

            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());

            if(!email.equals("") && !password.equals("")){
                userViewModel.signIn(email, password).observe(getViewLifecycleOwner(), signInObserver);
            }
            else{
                Snackbar.make(view, getString(R.string.authenticationFailed),
                        Snackbar.LENGTH_SHORT).show();
            }

        });
    }

    /*
        // TODO: capire se sta roba serve o meno

        DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(requireContext());
        try {
            if (!dataEncryptionUtil.readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME).isEmpty()) {
                SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(requireContext());
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
         */

    private void navigateTo(int destination, boolean finishActivity) {
        Navigation.findNavController(requireView()).navigate(destination);
        if (finishActivity) {
            requireActivity().finish();
        }
    }

    private void navigateTo(int destination, boolean finishActivity, boolean fromProfileFragment) {

        Bundle bundle = new Bundle();
        bundle.putBoolean("fromProfileFragment", fromProfileFragment);
        Navigation.findNavController(requireView()).navigate(destination, bundle);
        if (finishActivity) {
            requireActivity().finish();
        }
    }
}