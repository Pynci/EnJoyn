package it.unimib.enjoyn.ui;

import static it.unimib.enjoyn.util.Constants.AUTHENTICATION_ERROR;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.util.ServiceLocator;

public class UserViewModel extends ViewModel {

    private FirebaseAuth auth;

    private MutableLiveData<Result> userByUsername;
    private MutableLiveData<Result> userByEmail;
    private MutableLiveData<Result> signUpResult;
    private MutableLiveData<Result> signInResult;
    private MutableLiveData<Result> registerImageResult;
    private MutableLiveData<Result> registerNameAndSurnameResult;

    private final IUserRepository userRepository;


    public UserViewModel() {
        auth = FirebaseAuth.getInstance();
        userRepository = ServiceLocator.getInstance().getUserRepository(false);
        userByUsername = new MutableLiveData<>();
        userByEmail = new MutableLiveData<>();
        signUpResult = new MutableLiveData<>();
        signInResult = new MutableLiveData<>();
        //resultAddUser = new MutableLiveData<>();
    }

    public MutableLiveData<Result> signUp(String email, String password, String username){
        signUpResult = userRepository.createUser(email, password, username);
        return signUpResult;
    }


    public MutableLiveData<Result> signIn(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                        signInResult.postValue(new Result.SignInSuccess(auth.getCurrentUser()));
                   }
                   else{
                       signInResult.postValue(new Result.Error(AUTHENTICATION_ERROR));
                   }
                });

        return signInResult;
    }


    public MutableLiveData<Result> getUserByUsername(String username){
        userByUsername = userRepository.getUserByUsername(username);
        return userByUsername;
    }

    public MutableLiveData<Result> getUserByEmail(String email){
        userByEmail = userRepository.getUserByEmail(email);
        return userByEmail;
    }

    public MutableLiveData<Result> registerUserImage(Uri uri) {
        registerImageResult = userRepository.createUserImage(uri);
        return registerImageResult;
    }

    public MutableLiveData<Result> registerUserNameAndSurname(String nome, String cognome) {
        registerNameAndSurnameResult = userRepository.updateNameAndSurname(nome, cognome);
        return registerNameAndSurnameResult;
    }

    public String checkEmail(String email) {
        if(email == null || email.length() == 0)
            return "empty";
        if (!(EmailValidator.getInstance().isValid(email)))
            return "invalid";
        else
            return "ok";
    }

    public String checkPassword(String password) {
        boolean number = false;
        boolean specialChar = false;
        boolean uppercaseChar = false;
        if (password == null || password.length()==0) {
            return "empty";
        }
        if(password.length() < 8) {
            return "short";
        }

        for (int i = 0; i < password.length(); i++) {
            if (password.charAt(i) >= '0' && password.charAt(i) <= '9') {
                number = true;
            }
            if (password.charAt(i) >= 'A' && password.charAt(i) <= 'Z') {
                uppercaseChar = true;
            }
            if (password.charAt(i) >= '!' && password.charAt(i) <= '/') {
                specialChar = true;
            }

        }


        if (!number) {
            return "number_missing";
        }
        else if (!uppercaseChar) {
            return "uppercaseChar_missing";
        } else if (!specialChar) {
            return "specialChar_missing";
        }

        return "ok";
    }


    public String checkConfirmPassword(String password, String confirmPassword) {

        if(confirmPassword == null || confirmPassword.length() == 0) {
            return "empty";
        } else if (!(confirmPassword.equals(password))) {
            return "not_equal";
        }

        return "ok";
    }

    public String checkUsername(String username){

        if (username == null || username.length() == 0) {
            return "empty";
        }
        if(username.length() >= 20) {
            return "too_long";
        }

        for (int i = 0; i < username.length(); i++) {
            if(Character.isWhitespace(username.charAt(i))){
                return "has_whitespace";
            }
        }

        return "ok";
    }

    public String checkForNumbersAndSpecialCharacters(String name) {

        boolean containsForbiddenCharacters = false;

        for (int i = 0; i < name.length(); i++) {

            if (name.charAt(i) >= '0' && name.charAt(i) <= '9' ||
                name.charAt(i) >= '!' && name.charAt(i) <= '/') {
                containsForbiddenCharacters = true;
            }
        }

        if(containsForbiddenCharacters)
            return "has_forbidden_characters";
        else
            return "ok";
    }
}
