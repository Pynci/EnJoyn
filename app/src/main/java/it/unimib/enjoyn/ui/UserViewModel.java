package it.unimib.enjoyn.ui;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.util.ServiceLocator;

public class UserViewModel extends ViewModel {

    private final IUserRepository userRepository;

    public UserViewModel() {
        userRepository = ServiceLocator.getInstance().getUserRepository();
    }

    public MutableLiveData<Result> signUp(String email, String password, String username){
        return userRepository.createUser(email, password, username);
    }

    public MutableLiveData<Result> signIn(String email, String password){
        return userRepository.signIn(email, password);
    }

    public MutableLiveData<Result> signOut(){
        return userRepository.signOut();
    }

    public MutableLiveData<Result> sendEmailVerification(){
        return userRepository.sendEmailVerification();
    }


    public MutableLiveData<Result> sendResetPasswordEmail(String email) {
        return userRepository.sendResetPasswordEmail(email);
    }

    public MutableLiveData<Result> getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }

    public MutableLiveData<Result> getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public User getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public MutableLiveData<Result> setUserPropic(Uri uri) {
        return userRepository.updatePropic(uri);
    }

    public MutableLiveData<Result> setUserNameAndSurname(String name, String surname) {
        return userRepository.updateNameAndSurname(name, surname);
    }

    public MutableLiveData<Result> setUserDescription(String description) {
        return userRepository.updateDescription(description);
    }

    public MutableLiveData<Result> updateEmailVerificationStatus(){
        return userRepository.updateEmailVerificationStatus();
    }

    public MutableLiveData<Result> updateProfileConfigurationStatus(){
        return userRepository.updateProfileConfigurationStatus();
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

        return new MutableLiveData<>(allresults);
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
