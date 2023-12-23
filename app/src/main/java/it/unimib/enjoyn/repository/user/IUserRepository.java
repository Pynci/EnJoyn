package it.unimib.enjoyn.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.User;

public interface IUserRepository {

    MutableLiveData<Exception> addUser(User user);

    MutableLiveData<User> getUser(String email);
}
