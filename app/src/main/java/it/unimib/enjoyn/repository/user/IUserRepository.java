package it.unimib.enjoyn.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.User;

public interface IUserRepository {

    MutableLiveData<Exception> addUser(String email, String password, String username);

    MutableLiveData<User> getUser(String email);
}
