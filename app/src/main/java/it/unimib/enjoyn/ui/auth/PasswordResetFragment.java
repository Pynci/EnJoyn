package it.unimib.enjoyn.ui.auth;

import static it.unimib.enjoyn.util.Constants.EMAIL_RESET_PASSWORD_SENDING_ERROR;

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

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import it.unimib.enjoyn.R;
import it.unimib.enjoyn.databinding.FragmentPasswordResetBinding;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.util.ErrorMessagesUtil;
import it.unimib.enjoyn.util.SnackbarBuilder;
import it.unimib.enjoyn.ui.viewmodels.UserViewModel;

public class PasswordResetFragment extends Fragment {

    private FragmentPasswordResetBinding fragmentPasswordResetBinding;
    private Observer<Result> emailRecoverPasswordObserver;
    private UserViewModel userViewModel;

    public PasswordResetFragment() {
        // Required empty public constructor
    }

    public static PasswordResetFragment newInstance() {
        return new PasswordResetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentPasswordResetBinding = FragmentPasswordResetBinding.inflate(inflater, container, false);
        return fragmentPasswordResetBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextInputLayout textInputEmail = fragmentPasswordResetBinding.fragmentPasswordResetTextInputLayoutEmail;
        TextInputEditText emailProvided = fragmentPasswordResetBinding.fragmentPasswordResetTextInputEditTextEmail;
        Button buttonNext = fragmentPasswordResetBinding.fragmentPasswordResetButtonNext;
        int currentTheme = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        ErrorMessagesUtil errorMessagesUtil = new ErrorMessagesUtil(requireActivity().getApplication());

        emailRecoverPasswordObserver = result -> {
            if (result.isSuccessful()){
                Navigation
                        .findNavController(view)
                        .navigate(R.id.action_passwordResetFragment_to_loginFragment);
            }
            else {
                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(errorMessagesUtil.getUserErrorMessage(((Result.Error) result).getMessage()), view, getContext(), currentTheme);
                snackbar.show();
            }
        };

        emailProvided.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String email = String.valueOf(emailProvided.getText());
                String result = userViewModel.checkEmail(email);

                switch(result){
                    case "ok":
                        textInputEmail.setError(null);
                        break;
                    case "empty":
                        textInputEmail.setError(getString(R.string.informationRequiredError));
                        break;
                    case "invalid":
                        textInputEmail.setError(getString(R.string.invalidEmail));
                        break;
                }
            }
            else {
                textInputEmail.setError(null);
            }
        });

        buttonNext.setOnClickListener(v -> {

            view.clearFocus();

            if(userViewModel.checkEmail(String.valueOf(emailProvided.getText())).equals("ok")){
                userViewModel
                        .sendResetPasswordEmail(String.valueOf(emailProvided.getText()))
                        .observe(this.requireActivity(), emailRecoverPasswordObserver);

                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildOkSnackbar(R.string.email_sent_to_specified_address, view, getContext(), currentTheme);
                snackbar.show();
            }
            else{

                Snackbar snackbar;
                snackbar = SnackbarBuilder.buildErrorSnackbar(errorMessagesUtil.getUserErrorMessage(EMAIL_RESET_PASSWORD_SENDING_ERROR), view, getContext(), currentTheme);
                snackbar.show();
            }
        });
    }
}