package it.unimib.enjoyn.ui;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import org.apache.commons.validator.routines.EmailValidator;

import javax.annotation.Nullable;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.util.ServiceLocator;

public class UserViewModel extends ViewModel {

    private MutableLiveData<Result> userByUsernameResult;
    private MutableLiveData<Result> userByEmailResult;
    private MutableLiveData<Result> currentUserResult;
    private MutableLiveData<Result> signUpResult;
    private MutableLiveData<Result> signInResult;
    private MutableLiveData<Result> emailVerificationSendingResult;
    private MutableLiveData<Result> setUserPropicResult;
    private MutableLiveData<Result> setUserNameAndSurnameResult;
    private MutableLiveData<Result> setUserDescriptionResult;
    private MutableLiveData<Result> setUserOptionalParametersResult;
    private MutableLiveData<Result> resetPasswordEmailSendResult;

    private final IUserRepository userRepository;

    public UserViewModel() {
        userRepository = ServiceLocator.getInstance().getUserRepository(false);
    }

    public MutableLiveData<Result> signUp(String email, String password, String username){
        signUpResult = userRepository.createUser(email, password, username);
        return signUpResult;
    }

    public MutableLiveData<Result> sendEmailVerification(){
        emailVerificationSendingResult = userRepository.sendEmailVerification();
        return emailVerificationSendingResult;
    }

    public MutableLiveData<Result> signIn(String email, String password){
        signInResult = userRepository.signIn(email, password);
        return signInResult;
    }


    public MutableLiveData<Result> getUserByUsername(String username){
        userByUsernameResult = userRepository.getUserByUsername(username);
        return userByUsernameResult;
    }

    public MutableLiveData<Result> getUserByEmail(String email){
        userByEmailResult = userRepository.getUserByEmail(email);
        return userByEmailResult;
    }

    public MutableLiveData<Result> getCurrentUser(){
        currentUserResult = userRepository.getCurrentUser();
        return currentUserResult;
    }

    public MutableLiveData<Result> setUserPropic(Uri uri) {
        setUserPropicResult = userRepository.createPropic(uri);
        return setUserPropicResult;
    }

    public MutableLiveData<Result> setUserNameAndSurname(String name, String surname) {
        setUserNameAndSurnameResult = userRepository.createNameAndSurname(name, surname);
        return setUserNameAndSurnameResult;
    }

    public MutableLiveData<Result> setUserDescription(String description) {
        setUserDescriptionResult = userRepository.createUserDescription(description);
        return setUserDescriptionResult;
    }

    public MutableLiveData<Result> setOptionalUserParameters(String name, String surname,
                                                             String description, Uri uri) {


        Result.ResultList allresults = new Result.ResultList();

        if(name != null && surname != null && !name.equals("") && !surname.equals("")) {
            setUserNameAndSurname(name, surname).observeForever(allresults::addResult);
        }
        if(description != null && !description.equals("")) {
            setUserDescription(description).observeForever(allresults::addResult);
        }
        if(uri != null) {
            setUserPropic(uri).observeForever(allresults::addResult);
        }

        setUserOptionalParametersResult = new MutableLiveData<>(allresults);
        return setUserOptionalParametersResult;
    }

    public MutableLiveData<Result> sendResetPasswordEmail(String email) {
        resetPasswordEmailSendResult = userRepository.sendResetPasswordEmail(email);
        return resetPasswordEmailSendResult;
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
