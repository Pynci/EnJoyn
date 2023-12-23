package it.unimib.enjoyn.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.enjoyn.model.User;
import it.unimib.enjoyn.repository.user.IUserRepository;
import it.unimib.enjoyn.util.ServiceLocator;

public class UserViewModel extends ViewModel {

    private final MutableLiveData<User> user;
    private final IUserRepository userRepository;

    public UserViewModel() {
        user = new MutableLiveData<>();
        userRepository = ServiceLocator.getInstance().getUserRepository(false);
    }

    public int checkEmail(String email) {
        if(email == null || email.length() == 0)
            return 1;
        if (!(EmailValidator.getInstance().isValid(email)))
            return 2;
        else
            return 0;
    }

    public boolean isPasswordOkForLogin(String password){
        return password == null || password.length() == 0;
    }
}
