package it.unimib.enjoyn.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.util.ServiceLocator;

public class UserViewModel extends ViewModel {

    private MutableLiveData<User> user;
    private MutableLiveData<Exception> resultAddUser;
    private final IUserRepository userRepository;


    public UserViewModel() {
        user = new MutableLiveData<>();
        userRepository = ServiceLocator.getInstance().getUserRepository(false);
        //resultAddUser = new MutableLiveData<>();
    }

    public MutableLiveData<Exception> addUser(String email, String password, String username){
        resultAddUser = userRepository.addUser(email, password, username);
        return resultAddUser;
    }

    public String checkEmail(String email) {
        if(email == null || email.length() == 0)
            return "empty";
        if (!(EmailValidator.getInstance().isValid(email)))
            return "invalid";
        else
            return "ok";
    }


    public boolean isPasswordOkForLogin(String password){
        return password == null || password.length() == 0;
    }


    public String checkPassword(String password) {
        boolean number = false;
        boolean specialChar = false;
        boolean uppercaseChar = false;
        if (password == null || password.length()==0) {
            return "empty";
        }
        if(password.length() < 8) {
            //textInputPassword.setError(getString(R.string.tooShortPassword));
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

        //TODO: aggiungere controllo di esistenza sul db

        return "ok";
    }
}
